package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import com.fiap.hospital.bff.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para FindByGatewayImpl
 * 
 * Testa as regras de negócio relacionadas à busca de usuários:
 * - Busca de todos os usuários
 * - Busca de usuário por ID
 * - Busca de usuário por email
 * - Tratamento de casos não encontrados
 * 
 * Utiliza mocks para isolar as dependências externas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FindByGateway - Testes Unitários")
class FindByGatewayImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private FindByGatewayImpl findByGateway;

    @Nested
    @DisplayName("Testes de Busca de Todos os Usuários")
    class GetAllUsersTests {

        @Test
        @DisplayName("Deve retornar lista de usuários quando existem registros")
        void shouldReturnUsersList_WhenRecordsExist() {
            // Arrange
            UserEntity entity1 = TestDataBuilder.createValidUserEntity();
            entity1.setId(1L);
            UserEntity entity2 = TestDataBuilder.createValidUserEntity();
            entity2.setId(2L);
            entity2.setEmail("medico@hospital.com");
            
            List<UserEntity> entities = Arrays.asList(entity1, entity2);
            
            User user1 = TestDataBuilder.createValidUserDomain();
            User user2 = TestDataBuilder.createValidDoctorDomain();
            
            when(userRepository.findAll()).thenReturn(entities);
            when(userMapper.toUserDomain(any(UserEntity.class))).thenReturn(user1, user2);

            // Act
            List<User> actualUsers = findByGateway.getAll();

            // Assert
            assertThat(actualUsers).isNotNull();
            assertThat(actualUsers).hasSize(2);
            assertThat(actualUsers).containsExactly(user1, user2);
            
            verify(userRepository, times(1)).findAll();
            verify(userMapper, times(2)).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não existem registros")
        void shouldReturnEmptyList_WhenNoRecordsExist() {
            // Arrange
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<User> actualUsers = findByGateway.getAll();

            // Assert
            assertThat(actualUsers).isNotNull();
            assertThat(actualUsers).isEmpty();
            
            verify(userRepository, times(1)).findAll();
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve propagar exceção quando repositório falha")
        void shouldPropagateException_WhenRepositoryFails() {
            // Arrange
            RuntimeException repositoryError = new RuntimeException("Erro na busca");
            when(userRepository.findAll()).thenThrow(repositoryError);

            // Act & Assert
            assertThatThrownBy(() -> findByGateway.getAll())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na busca");
            
            verify(userRepository, times(1)).findAll();
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Usuário por ID")
    class GetUserByIdTests {

        @Test
        @DisplayName("Deve retornar usuário quando ID existe")
        void shouldReturnUser_WhenIdExists() {
            // Arrange
            Long userId = 1L;
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            userEntity.setId(userId);
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(eq(userId))).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(any(UserEntity.class))).thenReturn(expectedUser);

            // Act
            Optional<User> actualUser = findByGateway.getById(userId);

            // Assert
            assertThat(actualUser).isPresent();
            assertThat(actualUser.get()).isEqualTo(expectedUser);
            
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, times(1)).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não é encontrado por ID")
        void shouldThrowException_WhenUserNotFoundById() {
            // Arrange
            Long nonExistentId = 999L;
            when(userRepository.findById(eq(nonExistentId))).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> findByGateway.getById(nonExistentId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(nonExistentId.toString())
                    .hasMessageContaining("not found");
            
            verify(userRepository, times(1)).findById(nonExistentId);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve propagar exceção quando repositório falha na busca por ID")
        void shouldPropagateException_WhenRepositoryFailsOnFindById() {
            // Arrange
            Long userId = 1L;
            RuntimeException repositoryError = new RuntimeException("Erro no banco");
            when(userRepository.findById(eq(userId))).thenThrow(repositoryError);

            // Act & Assert
            assertThatThrownBy(() -> findByGateway.getById(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no banco");
            
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Usuário por Email")
    class FindUserByEmailTests {

        @Test
        @DisplayName("Deve retornar usuário quando email existe")
        void shouldReturnUser_WhenEmailExists() {
            // Arrange
            String email = "joao.silva@hospital.com";
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            userEntity.setEmail(email);
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(any(UserEntity.class))).thenReturn(expectedUser);

            // Act
            Optional<User> actualUser = findByGateway.findByEmail(email);

            // Assert
            assertThat(actualUser).isPresent();
            assertThat(actualUser.get()).isEqualTo(expectedUser);
            assertThat(actualUser.get().getEmail()).isEqualTo(email);
            
            verify(userRepository, times(1)).findByEmail(email);
            verify(userMapper, times(1)).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve retornar Optional.empty quando email não existe")
        void shouldReturnEmptyOptional_WhenEmailDoesNotExist() {
            // Arrange
            String nonExistentEmail = "naoexiste@hospital.com";
            when(userRepository.findByEmail(eq(nonExistentEmail))).thenReturn(Optional.empty());

            // Act
            Optional<User> actualUser = findByGateway.findByEmail(nonExistentEmail);

            // Assert
            assertThat(actualUser).isEmpty();
            
            verify(userRepository, times(1)).findByEmail(nonExistentEmail);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve aceitar diferentes formatos de email válidos")
        void shouldAcceptDifferentValidEmailFormats() {
            // Arrange
            String[] validEmails = {
                "usuario@hospital.com",
                "medico.especialista@hospital.com.br",
                "enfermeiro123@clinica.org",
                "admin@hospital-teste.com"
            };
            
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            for (String email : validEmails) {
                when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(userEntity));
                when(userMapper.toUserDomain(any(UserEntity.class))).thenReturn(expectedUser);

                // Act
                Optional<User> actualUser = findByGateway.findByEmail(email);

                // Assert
                assertThat(actualUser).isPresent();
                
                verify(userRepository, times(1)).findByEmail(email);
                reset(userRepository, userMapper); // Reset para próxima iteração
            }
        }

        @Test
        @DisplayName("Deve propagar exceção quando repositório falha na busca por email")
        void shouldPropagateException_WhenRepositoryFailsOnFindByEmail() {
            // Arrange
            String email = "joao.silva@hospital.com";
            RuntimeException repositoryError = new RuntimeException("Erro na consulta");
            when(userRepository.findByEmail(eq(email))).thenThrow(repositoryError);

            // Act & Assert
            assertThatThrownBy(() -> findByGateway.findByEmail(email))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na consulta");
            
            verify(userRepository, times(1)).findByEmail(email);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes de Integração entre Métodos")
    class IntegrationTests {

        @Test
        @DisplayName("Deve manter consistência entre getAll e getById")
        void shouldMaintainConsistency_BetweenGetAllAndGetById() {
            // Arrange
            UserEntity entity1 = TestDataBuilder.createValidUserEntity();
            entity1.setId(1L);
            UserEntity entity2 = TestDataBuilder.createValidUserEntity();
            entity2.setId(2L);
            
            User user1 = TestDataBuilder.createValidUserDomain();
            User user2 = TestDataBuilder.createValidUserDomain();

            // Configurar mocks para getAll
            when(userRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
            when(userMapper.toUserDomain(any(UserEntity.class))).thenReturn(user1, user2);

            // Configurar mocks para getById
            when(userRepository.findById(eq(1L))).thenReturn(Optional.of(entity1));
            when(userRepository.findById(eq(2L))).thenReturn(Optional.of(entity2));

            // Act
            List<User> allUsers = findByGateway.getAll();
            Optional<User> userById1 = findByGateway.getById(1L);
            Optional<User> userById2 = findByGateway.getById(2L);

            // Assert
            assertThat(allUsers).hasSize(2);
            assertThat(userById1).isPresent();
            assertThat(userById2).isPresent();
            
            // Os usuários retornados devem ser consistentes
            assertThat(userById1.get().getNome()).isEqualTo(user1.getNome());
            assertThat(userById1.get().getEmail()).isEqualTo(user1.getEmail());
            assertThat(userById2.get().getNome()).isEqualTo(user2.getNome());
            assertThat(userById2.get().getEmail()).isEqualTo(user2.getEmail());
        }
    }
}
package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.FindByGateway;
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
 * Testes unitários para UserQueryUseCaseImpl
 * 
 * Testa as regras de negócio relacionadas às consultas de usuário:
 * - Busca de todos os usuários
 * - Busca de usuário por ID
 * 
 * Utiliza mocks para isolar as dependências externas (gateways)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserQueryUseCase - Testes Unitários")
class UserQueryUseCaseImplTest {

    @Mock
    private FindByGateway findByGateway;
    
    @InjectMocks
    private UserQueryUseCaseImpl userQueryUseCase;

    @Nested
    @DisplayName("Testes de Busca de Todos os Usuários")
    class GetAllUsersTests {

        @Test
        @DisplayName("Deve retornar lista de usuários quando existem usuários cadastrados")
        void shouldReturnUsersList_WhenUsersExist() {
            // Arrange
            User user1 = TestDataBuilder.createValidUserDomain();
            User user2 = TestDataBuilder.createValidDoctorDomain();
            List<User> expectedUsers = Arrays.asList(user1, user2);
            
            when(findByGateway.getAll()).thenReturn(expectedUsers);

            // Act
            List<User> actualUsers = userQueryUseCase.getAllUsers();

            // Assert
            assertThat(actualUsers).isNotNull();
            assertThat(actualUsers).hasSize(2);
            assertThat(actualUsers).containsExactlyInAnyOrder(user1, user2);
            
            // Verificar propriedades específicas
            assertThat(actualUsers.get(0).getNome()).isNotBlank();
            assertThat(actualUsers.get(0).getEmail()).isNotBlank();
            assertThat(actualUsers.get(0).getTipo()).isNotBlank();
            
            verify(findByGateway, times(1)).getAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não existem usuários cadastrados")
        void shouldReturnEmptyList_WhenNoUsersExist() {
            // Arrange
            when(findByGateway.getAll()).thenReturn(Collections.emptyList());

            // Act
            List<User> actualUsers = userQueryUseCase.getAllUsers();

            // Assert
            assertThat(actualUsers).isNotNull();
            assertThat(actualUsers).isEmpty();
            
            verify(findByGateway, times(1)).getAll();
        }

        @Test
        @DisplayName("Deve propagar exceção quando gateway falha na busca de todos usuários")
        void shouldPropagateException_WhenGatewayFailsOnGetAll() {
            // Arrange
            RuntimeException expectedError = new RuntimeException("Erro na busca de usuários");
            when(findByGateway.getAll()).thenThrow(expectedError);

            // Act & Assert
            assertThatThrownBy(() -> userQueryUseCase.getAllUsers())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na busca de usuários");
            
            verify(findByGateway, times(1)).getAll();
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Usuário por ID")
    class GetUserByIdTests {

        @Test
        @DisplayName("Deve retornar usuário quando ID válido e existente é fornecido")
        void shouldReturnUser_WhenValidAndExistingIdProvided() {
            // Arrange
            Long userId = 1L;
            User expectedUser = TestDataBuilder.createValidUserDomain();
            
            when(findByGateway.getById(eq(userId))).thenReturn(Optional.of(expectedUser));

            // Act
            Optional<User> actualUser = userQueryUseCase.getUserById(userId);

            // Assert
            assertThat(actualUser).isPresent();
            assertThat(actualUser.get().getNome()).isEqualTo(expectedUser.getNome());
            assertThat(actualUser.get().getEmail()).isEqualTo(expectedUser.getEmail());
            assertThat(actualUser.get().getLogin()).isEqualTo(expectedUser.getLogin());
            assertThat(actualUser.get().getTipo()).isEqualTo(expectedUser.getTipo());
            
            verify(findByGateway, times(1)).getById(userId);
        }

        @Test
        @DisplayName("Deve retornar Optional.empty quando usuário não existe")
        void shouldReturnEmptyOptional_WhenUserDoesNotExist() {
            // Arrange
            Long nonExistentUserId = 999L;
            
            when(findByGateway.getById(eq(nonExistentUserId))).thenReturn(Optional.empty());

            // Act
            Optional<User> actualUser = userQueryUseCase.getUserById(nonExistentUserId);

            // Assert
            assertThat(actualUser).isEmpty();
            
            verify(findByGateway, times(1)).getById(nonExistentUserId);
        }

        @Test
        @DisplayName("Deve propagar exceção quando gateway falha na busca por ID")
        void shouldPropagateException_WhenGatewayFailsOnGetById() {
            // Arrange
            Long userId = 1L;
            RuntimeException expectedError = new RuntimeException("Erro na busca por ID");
            
            when(findByGateway.getById(eq(userId))).thenThrow(expectedError);

            // Act & Assert
            assertThatThrownBy(() -> userQueryUseCase.getUserById(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na busca por ID");
            
            verify(findByGateway, times(1)).getById(userId);
        }

        @Test
        @DisplayName("Deve aceitar diferentes tipos de ID válidos")
        void shouldAcceptDifferentValidIdTypes() {
            // Arrange
            Long[] testIds = {1L, 100L, 999L, Long.MAX_VALUE};
            User expectedUser = TestDataBuilder.createValidUserDomain();
            
            for (Long testId : testIds) {
                when(findByGateway.getById(eq(testId))).thenReturn(Optional.of(expectedUser));
                
                // Act
                Optional<User> actualUser = userQueryUseCase.getUserById(testId);
                
                // Assert
                assertThat(actualUser).isPresent();
                assertThat(actualUser.get()).isEqualTo(expectedUser);
            }
            
            verify(findByGateway, times(testIds.length)).getById(any(Long.class));
        }
    }
}
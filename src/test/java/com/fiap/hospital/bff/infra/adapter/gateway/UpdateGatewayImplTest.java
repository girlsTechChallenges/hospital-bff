package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UpdateGatewayImpl
 * 
 * Testa as operações de atualização de usuários:
 * - Atualização de dados do usuário
 * - Atualização de senha
 * - Validação de cenários de erro
 * 
 * Utiliza mocks para isolar dependências externas (repository, mapper, passwordEncoder)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateGateway - Testes Unitários")
class UpdateGatewayImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateGatewayImpl updateGateway;

    @Nested
    @DisplayName("Testes de Atualização de Usuário")
    class UpdateUserTests {

        @Test
        @DisplayName("Deve atualizar usuário com sucesso quando ID existe e dados são válidos")
        void shouldUpdateUserSuccessfully_WhenIdExistsAndDataIsValid() {
            // Arrange
            Long userId = 1L;
            User inputUser = TestDataBuilder.createValidUserDomain();
            UserEntity existingEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
            when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
            when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(savedEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = updateGateway.update(userId, inputUser);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedUser);
            
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).save(any(UserEntity.class));
            verify(userMapper, times(1)).toUserDomain(savedEntity);
        }

        @Test
        @DisplayName("Deve retornar Optional.empty quando usuário não existe")
        void shouldReturnEmptyOptional_WhenUserDoesNotExist() {
            // Arrange
            Long nonExistentId = 999L;
            User inputUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act
            Optional<User> result = updateGateway.update(nonExistentId, inputUser);

            // Assert
            assertThat(result).isEmpty();
            
            verify(userRepository, times(1)).findById(nonExistentId);
            verify(userRepository, never()).save(any(UserEntity.class));
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve atualizar apenas campos não nulos")
        void shouldUpdateOnlyNonNullFields() {
            // Arrange
            Long userId = 1L;
            User partialUpdate = new User(null, "novo@email.com", null, null, null);
            UserEntity existingEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
            when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(savedEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = updateGateway.update(userId, partialUpdate);

            // Assert
            assertThat(result).isPresent();
            verify(passwordEncoder, never()).encode(anyString()); // Senha não deve ser atualizada se for null
        }

        @Test
        @DisplayName("Deve codificar senha quando fornecida")
        void shouldEncodePassword_WhenPasswordProvided() {
            // Arrange
            Long userId = 1L;
            String newPassword = "novaSenha123";
            String encodedPassword = "encoded_nova_senha";
            User inputUser = new User(null, null, null, newPassword, null);
            UserEntity existingEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
            when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
            when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(savedEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = updateGateway.update(userId, inputUser);

            // Assert
            assertThat(result).isPresent();
            verify(passwordEncoder, times(1)).encode(newPassword);
        }

        @Test
        @DisplayName("Deve ignorar senha vazia")
        void shouldIgnoreEmptyPassword() {
            // Arrange
            Long userId = 1L;
            User inputUser = new User(null, null, null, "", null);
            UserEntity existingEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
            when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(savedEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = updateGateway.update(userId, inputUser);

            // Assert
            assertThat(result).isPresent();
            verify(passwordEncoder, never()).encode(anyString());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Senha")
    class UpdatePasswordTests {

        @Test
        @DisplayName("Deve atualizar senha com sucesso quando usuário existe")
        void shouldUpdatePasswordSuccessfully_WhenUserExists() {
            // Arrange
            String email = "joao@hospital.com";
            String newPassword = "novaSenha123";
            String encodedPassword = "encoded_password";
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
            when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

            // Act
            updateGateway.updatePassword(email, newPassword);

            // Assert
            verify(userRepository, times(1)).findByEmail(email);
            verify(passwordEncoder, times(1)).encode(newPassword);
            verify(userRepository, times(1)).save(userEntity);
            assertThat(userEntity.getSenha()).isEqualTo(encodedPassword);
        }

        @Test
        @DisplayName("Deve ignorar atualização quando usuário não existe")
        void shouldIgnoreUpdate_WhenUserDoesNotExist() {
            // Arrange
            String nonExistentEmail = "naoexiste@hospital.com";
            String newPassword = "novaSenha123";

            when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

            // Act
            updateGateway.updatePassword(nonExistentEmail, newPassword);

            // Assert
            verify(userRepository, times(1)).findByEmail(nonExistentEmail);
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(UserEntity.class));
        }

        @Test
        @DisplayName("Deve tratar exceção do encoder graciosamente")
        void shouldHandleEncoderExceptionGracefully() {
            // Arrange
            String email = "joao@hospital.com";
            String newPassword = "novaSenha123";
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            RuntimeException encoderException = new RuntimeException("Encoder error");

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
            when(passwordEncoder.encode(newPassword)).thenThrow(encoderException);

            // Act & Assert
            assertThatThrownBy(() -> updateGateway.updatePassword(email, newPassword))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Encoder error");

            verify(userRepository, times(1)).findByEmail(email);
            verify(passwordEncoder, times(1)).encode(newPassword);
            verify(userRepository, never()).save(any(UserEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes de Integração")
    class IntegrationTests {

        @Test
        @DisplayName("Deve funcionar corretamente quando todos os componentes estão integrados")
        void shouldWorkCorrectly_WhenAllComponentsAreIntegrated() {
            // Arrange
            Long userId = 1L;
            String email = "joao@hospital.com";
            String newPassword = "novaSenha123";
            
            User inputUser = TestDataBuilder.createValidUserDomain();
            inputUser.setEmail(email);
            inputUser.setSenha(newPassword);
            
            UserEntity existingEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
            when(passwordEncoder.encode(newPassword)).thenReturn("encoded_password");
            when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(savedEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = updateGateway.update(userId, inputUser);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedUser);
            
            // Verify integration
            verify(userRepository, times(1)).findById(userId);
            verify(passwordEncoder, times(1)).encode(newPassword);
            verify(userRepository, times(1)).save(any(UserEntity.class));
            verify(userMapper, times(1)).toUserDomain(savedEntity);
        }
    }
}
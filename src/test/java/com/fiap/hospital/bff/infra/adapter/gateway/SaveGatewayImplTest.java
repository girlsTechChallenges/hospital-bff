package com.fiap.hospital.bff.infra.adapter.gateway;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.exception.UserAlreadyRegisteredException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para SaveGatewayImpl
 * 
 * Testa as regras de negócio relacionadas ao salvamento de usuários:
 * - Criação de novos usuários
 * - Validação de unicidade de email
 * - Criptografia de senhas
 * - Integração com repositório e mapper
 * 
 * Utiliza mocks para isolar as dependências externas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SaveGateway - Testes Unitários")
class SaveGatewayImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    @InjectMocks
    private SaveGatewayImpl saveGateway;

    @Nested
    @DisplayName("Testes de Salvamento de Usuário")
    class SaveUserTests {

        @Test
        @DisplayName("Deve salvar usuário com sucesso quando email não existe")
        void shouldSaveUserSuccessfully_WhenEmailDoesNotExist() {
            // Arrange
            User inputUser = TestDataBuilder.createValidUserDomain();
            UserEntity mappedEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            savedEntity.setId(1L);
            User expectedUser = TestDataBuilder.createValidUserDomain();
            String encodedPassword = "$2a$10$encodedPassword";

            // Configurar mocks
            when(userRepository.findByEmail(eq(inputUser.getEmail()))).thenReturn(Optional.empty());
            when(passwordEncoder.encode("senha123456")).thenReturn(encodedPassword); // Senha original do TestDataBuilder
            when(userMapper.toUserEntity(any(User.class))).thenReturn(mappedEntity);
            when(userRepository.save(eq(mappedEntity))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(eq(savedEntity))).thenReturn(expectedUser);

            // Act
            User actualUser = saveGateway.save(inputUser);

            // Assert
            assertThat(actualUser).isNotNull();
            assertThat(actualUser).isEqualTo(expectedUser);

            // Verificar interações
            verify(userRepository, times(1)).findByEmail(inputUser.getEmail());
            verify(passwordEncoder, times(1)).encode("senha123456"); // Senha original do TestDataBuilder
            verify(userMapper, times(1)).toUserEntity(any(User.class));
            verify(userRepository, times(1)).save(mappedEntity);
            verify(userMapper, times(1)).toUserDomain(savedEntity);
        }

        @Test
        @DisplayName("Deve lançar exceção quando email já existe")
        void shouldThrowException_WhenEmailAlreadyExists() {
            // Arrange
            User inputUser = TestDataBuilder.createValidUserDomain();
            UserEntity existingUser = TestDataBuilder.createValidUserEntity();

            when(userRepository.findByEmail(eq(inputUser.getEmail()))).thenReturn(Optional.of(existingUser));

            // Act & Assert
            assertThatThrownBy(() -> saveGateway.save(inputUser))
                    .isInstanceOf(UserAlreadyRegisteredException.class)
                    .hasMessageContaining(inputUser.getEmail())
                    .hasMessageContaining("is already registered");

            // Verificar que não houve tentativa de salvar
            verify(userRepository, times(1)).findByEmail(inputUser.getEmail());
            verify(passwordEncoder, never()).encode(any());
            verify(userMapper, never()).toUserEntity(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve criptografar senha antes de salvar")
        void shouldEncryptPasswordBeforeSaving() {
            // Arrange
            User inputUser = TestDataBuilder.createValidUserDomain();
            String originalPassword = "senhaOriginal";
            String encodedPassword = "$2a$10$hashedPassword";
            inputUser.setSenha(originalPassword);

            UserEntity mappedEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findByEmail(eq(inputUser.getEmail()))).thenReturn(Optional.empty());
            when(passwordEncoder.encode(eq(originalPassword))).thenReturn(encodedPassword);
            when(userMapper.toUserEntity(any(User.class))).thenReturn(mappedEntity);
            when(userRepository.save(eq(mappedEntity))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(eq(savedEntity))).thenReturn(expectedUser);

            // Act
            saveGateway.save(inputUser);

            // Assert
            verify(passwordEncoder, times(1)).encode(originalPassword);
            
            // Verificar que a senha foi alterada no objeto domain antes do mapeamento
            assertThat(inputUser.getSenha()).isEqualTo(encodedPassword);
        }

        @Test
        @DisplayName("Deve propagar exceção quando repositório falha no save")
        void shouldPropagateException_WhenRepositoryFailsOnSave() {
            // Arrange
            User inputUser = TestDataBuilder.createValidUserDomain();
            UserEntity mappedEntity = TestDataBuilder.createValidUserEntity();
            String encodedPassword = "$2a$10$encodedPassword";
            RuntimeException repositoryError = new RuntimeException("Erro no banco de dados");

            when(userRepository.findByEmail(eq(inputUser.getEmail()))).thenReturn(Optional.empty());
            when(passwordEncoder.encode(eq(inputUser.getSenha()))).thenReturn(encodedPassword);
            when(userMapper.toUserEntity(any(User.class))).thenReturn(mappedEntity);
            when(userRepository.save(eq(mappedEntity))).thenThrow(repositoryError);

            // Act & Assert
            assertThatThrownBy(() -> saveGateway.save(inputUser))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no banco de dados");

            verify(userRepository, times(1)).save(mappedEntity);
        }

        @Test
        @DisplayName("Deve propagar exceção quando encoder falha na criptografia")
        void shouldPropagateException_WhenEncoderFailsOnEncryption() {
            // Arrange
            User inputUser = TestDataBuilder.createValidUserDomain();
            RuntimeException encoderError = new RuntimeException("Erro na criptografia");

            when(userRepository.findByEmail(eq(inputUser.getEmail()))).thenReturn(Optional.empty());
            when(passwordEncoder.encode(eq(inputUser.getSenha()))).thenThrow(encoderError);

            // Act & Assert
            assertThatThrownBy(() -> saveGateway.save(inputUser))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na criptografia");

            verify(passwordEncoder, times(1)).encode(inputUser.getSenha());
            verify(userMapper, never()).toUserEntity(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lidar com diferentes tipos de usuários")
        void shouldHandleDifferentUserTypes() {
            // Arrange - Testar MEDICO
            User doctorUser = TestDataBuilder.createValidDoctorDomain();
            setupSuccessfulSaveMocks(doctorUser);

            // Act
            User savedDoctor = saveGateway.save(doctorUser);

            // Assert
            assertThat(savedDoctor).isNotNull();
            verify(userRepository, times(1)).findByEmail(doctorUser.getEmail());

            // Reset mocks para próximo teste
            reset(userRepository, passwordEncoder, userMapper);

            // Arrange - Testar PACIENTE
            User patientUser = TestDataBuilder.createValidUserDomain();
            setupSuccessfulSaveMocks(patientUser);

            // Act
            User savedPatient = saveGateway.save(patientUser);

            // Assert
            assertThat(savedPatient).isNotNull();
            verify(userRepository, times(1)).findByEmail(patientUser.getEmail());
        }

        private void setupSuccessfulSaveMocks(User inputUser) {
            UserEntity mappedEntity = TestDataBuilder.createValidUserEntity();
            UserEntity savedEntity = TestDataBuilder.createValidUserEntity();
            savedEntity.setId(1L);
            User expectedUser = TestDataBuilder.createValidUserDomain();
            String encodedPassword = "$2a$10$encodedPassword";

            when(userRepository.findByEmail(eq(inputUser.getEmail()))).thenReturn(Optional.empty());
            when(passwordEncoder.encode(eq(inputUser.getSenha()))).thenReturn(encodedPassword);
            when(userMapper.toUserEntity(any(User.class))).thenReturn(mappedEntity);
            when(userRepository.save(eq(mappedEntity))).thenReturn(savedEntity);
            when(userMapper.toUserDomain(eq(savedEntity))).thenReturn(expectedUser);
        }
    }
}
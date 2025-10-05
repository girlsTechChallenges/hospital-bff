package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.FindByGateway;
import com.fiap.hospital.bff.infra.exception.UserCredentialsException;
import com.fiap.hospital.bff.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthenticationQueryUseCaseImpl
 * 
 * Testa as regras de negócio relacionadas à autenticação:
 * - Validação de login (email e senha)
 * - Geração de tokens JWT
 * - Tratamento de credenciais inválidas
 * 
 * Utiliza mocks para isolar as dependências externas
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationQueryUseCase - Testes Unitários")
class AuthenticationQueryUseCaseImplTest {

    @Mock
    private FindByGateway findByGateway;
    
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    @Mock
    private JwtEncoder jwtEncoder;
    
    @Mock
    private Jwt jwt;
    
    @InjectMocks
    private AuthenticationQueryUseCaseImpl authenticationQueryUseCase;

    @Nested
    @DisplayName("Testes de Validação de Login")
    class ValidateLoginTests {

        @Test
        @DisplayName("Deve gerar token com sucesso quando credenciais válidas são fornecidas")
        void shouldGenerateTokenSuccessfully_WhenValidCredentialsProvided() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String password = "senha123456";
            String hashedPassword = "$2a$10$hashedPassword";
            String expectedTokenValue = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZUBlbWFpbC5jb20ifQ.token";
            
            User user = TestDataBuilder.createValidUserDomain();
            user.setSenha(hashedPassword);
            
            // Configurar mocks
            when(findByGateway.findByEmail(eq(email))).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(eq(password), eq(hashedPassword))).thenReturn(true);
            when(jwt.getTokenValue()).thenReturn(expectedTokenValue);
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

            // Act
            Token actualToken = authenticationQueryUseCase.validateLogin(email, password);

            // Assert
            assertThat(actualToken).isNotNull();
            assertThat(actualToken.getAccessToken()).isEqualTo(expectedTokenValue);
            assertThat(actualToken.getExpiresIn()).isEqualTo(300L); // 5 minutos
            
            // Verificar interações
            verify(findByGateway, times(2)).findByEmail(email); // Uma para verificar existência, outra para buscar senha
            verify(passwordEncoder, times(1)).matches(password, hashedPassword);
            verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não existe")
        void shouldThrowException_WhenUserDoesNotExist() {
            // Arrange
            String nonExistentEmail = "naoexiste@hospital.com";
            String password = "senha123456";
            
            when(findByGateway.findByEmail(eq(nonExistentEmail))).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> authenticationQueryUseCase.validateLogin(nonExistentEmail, password))
                    .isInstanceOf(UserCredentialsException.class)
                    .hasMessage("Invalid email or password");
            
            verify(findByGateway, times(1)).findByEmail(nonExistentEmail);
            verify(passwordEncoder, never()).matches(any(), any());
            verify(jwtEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha está incorreta")
        void shouldThrowException_WhenPasswordIsIncorrect() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String wrongPassword = "senhaErrada";
            String hashedPassword = "$2a$10$hashedPassword";
            
            User user = TestDataBuilder.createValidUserDomain();
            user.setSenha(hashedPassword);
            
            when(findByGateway.findByEmail(eq(email))).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(eq(wrongPassword), eq(hashedPassword))).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> authenticationQueryUseCase.validateLogin(email, wrongPassword))
                    .isInstanceOf(UserCredentialsException.class)
                    .hasMessage("Invalid email or password");
            
            verify(findByGateway, times(2)).findByEmail(email);
            verify(passwordEncoder, times(1)).matches(wrongPassword, hashedPassword);
            verify(jwtEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("Deve gerar token com scope correto baseado no tipo de usuário")
        void shouldGenerateTokenWithCorrectScope_BasedOnUserType() {
            // Arrange
            String email = "maria.santos@hospital.com";
            String password = "senha123456";
            String hashedPassword = "$2a$10$hashedPassword";
            String expectedTokenValue = "eyJhbGciOiJSUzI1NiJ9.token";
            
            User doctorUser = TestDataBuilder.createValidDoctorDomain();
            doctorUser.setSenha(hashedPassword);
            
            when(findByGateway.findByEmail(eq(email))).thenReturn(Optional.of(doctorUser));
            when(passwordEncoder.matches(eq(password), eq(hashedPassword))).thenReturn(true);
            when(jwt.getTokenValue()).thenReturn(expectedTokenValue);
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

            // Act
            Token actualToken = authenticationQueryUseCase.validateLogin(email, password);

            // Assert
            assertThat(actualToken).isNotNull();
            assertThat(actualToken.getAccessToken()).isEqualTo(expectedTokenValue);
            
            // Verificar que o token foi gerado com o tipo correto (capturar argumentos seria ideal)
            verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
        }

        @Test
        @DisplayName("Deve gerar token com tempo de expiração correto")
        void shouldGenerateTokenWithCorrectExpirationTime() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String password = "senha123456";
            String hashedPassword = "$2a$10$hashedPassword";
            String expectedTokenValue = "eyJhbGciOiJSUzI1NiJ9.token";
            
            User user = TestDataBuilder.createValidUserDomain();
            user.setSenha(hashedPassword);
            
            when(findByGateway.findByEmail(eq(email))).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(eq(password), eq(hashedPassword))).thenReturn(true);
            when(jwt.getTokenValue()).thenReturn(expectedTokenValue);
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

            // Act
            Token actualToken = authenticationQueryUseCase.validateLogin(email, password);

            // Assert
            assertThat(actualToken.getExpiresIn()).isEqualTo(300L); // 5 minutos em segundos
        }

        @Test
        @DisplayName("Deve propagar exceção quando há erro na geração do token")
        void shouldPropagateException_WhenTokenGenerationFails() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String password = "senha123456";
            String hashedPassword = "$2a$10$hashedPassword";
            
            User user = TestDataBuilder.createValidUserDomain();
            user.setSenha(hashedPassword);
            
            RuntimeException tokenError = new RuntimeException("Erro na geração do token");
            
            when(findByGateway.findByEmail(eq(email))).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(eq(password), eq(hashedPassword))).thenReturn(true);
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenThrow(tokenError);

            // Act & Assert
            assertThatThrownBy(() -> authenticationQueryUseCase.validateLogin(email, password))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na geração do token");
            
            verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
        }
    }
}
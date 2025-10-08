package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.inputport.AuthenticationCommandUseCase;
import com.fiap.hospital.bff.core.inputport.AuthenticationQueryUseCase;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserAuthRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserCredentialsRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;
import com.fiap.hospital.bff.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthController
 * 
 * Testa a lógica de negócio dos endpoints REST de autenticação:
 * - POST /api/v1/auth/login - Login de usuário
 * - PATCH /api/v1/auth/password - Atualização de senha
 * 
 * Utiliza mocks para isolar use cases e mapper
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController - Testes Unitários")
class AuthControllerTest {

    @Mock
    private AuthenticationCommandUseCase authenticationCommandUseCase;

    @Mock
    private AuthenticationQueryUseCase authenticationQueryUseCase;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    @Nested
    @DisplayName("Testes de Login - POST /api/v1/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Deve realizar login com sucesso quando credenciais são válidas")
        void shouldLoginSuccessfully_WhenCredentialsAreValid() {
            // Arrange
            UserCredentialsRequestDto validCredentials = TestDataBuilder.createValidCredentials();
            Token mockToken = new Token("mock-jwt-token", 3600L);
            UserAuthRequestDto expectedResponse = new UserAuthRequestDto("mock-jwt-token", 3600L);

            when(authenticationQueryUseCase.validateLogin(
                eq(validCredentials.email()), 
                eq(validCredentials.password())
            )).thenReturn(mockToken);
            
            when(userMapper.toTokenResponseDto(mockToken)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<UserAuthRequestDto> response = authController.login(validCredentials);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().accessToken()).isEqualTo("mock-jwt-token");
            assertThat(response.getBody().expiresIn()).isEqualTo(3600L);

            verify(authenticationQueryUseCase, times(1))
                    .validateLogin(validCredentials.email(), validCredentials.password());
            verify(userMapper, times(1)).toTokenResponseDto(mockToken);
        }

        @Test
        @DisplayName("Deve retornar erro quando email é inválido")
        void shouldReturnError_WhenEmailIsInvalid() {
            // Arrange
            UserCredentialsRequestDto invalidCredentials = new UserCredentialsRequestDto("email-invalido", "senha123");

            when(authenticationQueryUseCase.validateLogin(
                eq(invalidCredentials.email()), 
                eq(invalidCredentials.password())
            )).thenThrow(new UserNotFoundException("Email não encontrado"));

            // Act & Assert
            assertThatThrownBy(() -> authController.login(invalidCredentials))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("User with email Email não encontrado not found");

            verify(authenticationQueryUseCase, times(1))
                    .validateLogin(invalidCredentials.email(), invalidCredentials.password());
            verify(userMapper, never()).toTokenResponseDto(any());
        }

        @Test
        @DisplayName("Deve retornar erro quando senha é inválida")
        void shouldReturnError_WhenPasswordIsInvalid() {
            // Arrange
            UserCredentialsRequestDto invalidCredentials = new UserCredentialsRequestDto("usuario@teste.com", "senha-errada");

            when(authenticationQueryUseCase.validateLogin(
                eq(invalidCredentials.email()), 
                eq(invalidCredentials.password())
            )).thenThrow(new RuntimeException("Senha inválida"));

            // Act & Assert
            assertThatThrownBy(() -> authController.login(invalidCredentials))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Senha inválida");

            verify(authenticationQueryUseCase, times(1))
                    .validateLogin(invalidCredentials.email(), invalidCredentials.password());
            verify(userMapper, never()).toTokenResponseDto(any());
        }

        @Test
        @DisplayName("Deve retornar erro quando credenciais são nulas")
        void shouldReturnError_WhenCredentialsAreNull() {
            // Act & Assert
            assertThatThrownBy(() -> authController.login(null))
                    .isInstanceOf(NullPointerException.class);

            verify(authenticationQueryUseCase, never()).validateLogin(anyString(), anyString());
            verify(userMapper, never()).toTokenResponseDto(any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Senha - PATCH /api/v1/auth/password")
    class UpdatePasswordTests {

        @Test
        @DisplayName("Deve atualizar senha com sucesso quando dados são válidos")
        void shouldUpdatePasswordSuccessfully_WhenDataIsValid() {
            // Arrange
            UserCredentialsRequestDto request = new UserCredentialsRequestDto("usuario@teste.com", "novaSenha123");

            // Act
            ResponseEntity<Void> response = authController.updatePassword(request);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();

            verify(authenticationCommandUseCase, times(1))
                    .updatePassword(request.email(), request.password());
        }

        @Test
        @DisplayName("Deve retornar erro quando email é inválido")
        void shouldReturnError_WhenEmailIsInvalid() {
            // Arrange
            UserCredentialsRequestDto request = new UserCredentialsRequestDto("email-invalido", "novaSenha123");

            doThrow(new UserNotFoundException("Email não encontrado"))
                    .when(authenticationCommandUseCase).updatePassword(request.email(), request.password());

            // Act & Assert
            assertThatThrownBy(() -> authController.updatePassword(request))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("User with email Email não encontrado not found");

            verify(authenticationCommandUseCase, times(1))
                    .updatePassword(request.email(), request.password());
        }

        @Test
        @DisplayName("Deve retornar erro quando nova senha é inválida")
        void shouldReturnError_WhenNewPasswordIsInvalid() {
            // Arrange
            UserCredentialsRequestDto request = new UserCredentialsRequestDto("usuario@teste.com", "123");

            doThrow(new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres"))
                    .when(authenticationCommandUseCase).updatePassword(request.email(), request.password());

            // Act & Assert
            assertThatThrownBy(() -> authController.updatePassword(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Senha deve ter pelo menos 6 caracteres");

            verify(authenticationCommandUseCase, times(1))
                    .updatePassword(request.email(), request.password());
        }

        @Test
        @DisplayName("Deve retornar erro quando request é nulo")
        void shouldReturnError_WhenRequestIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> authController.updatePassword(null))
                    .isInstanceOf(NullPointerException.class);

            verify(authenticationCommandUseCase, never()).updatePassword(anyString(), anyString());
        }

        @Test
        @DisplayName("Deve retornar erro quando dados são inválidos")
        void shouldReturnError_WhenDataIsInvalid() {
            // Arrange
            UserCredentialsRequestDto request = new UserCredentialsRequestDto("usuario@teste.com", null);

            doThrow(new IllegalArgumentException("Senha não pode ser nula"))
                    .when(authenticationCommandUseCase).updatePassword(request.email(), request.password());

            // Act & Assert
            assertThatThrownBy(() -> authController.updatePassword(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Senha não pode ser nula");

            verify(authenticationCommandUseCase, times(1))
                    .updatePassword(request.email(), request.password());
        }
    }
}
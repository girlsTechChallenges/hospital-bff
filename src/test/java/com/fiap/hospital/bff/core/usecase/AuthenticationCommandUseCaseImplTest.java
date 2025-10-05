package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthenticationCommandUseCaseImpl
 * 
 * Testa as regras de negócio relacionadas aos comandos de autenticação:
 * - Atualização de senha de usuário
 * 
 * Utiliza mocks para isolar as dependências externas (gateways)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationCommandUseCase - Testes Unitários")
class AuthenticationCommandUseCaseImplTest {

    @Mock
    private UpdateGateway updateGateway;
    
    @InjectMocks
    private AuthenticationCommandUseCaseImpl authenticationCommandUseCase;

    @Nested
    @DisplayName("Testes de Atualização de Senha")
    class UpdatePasswordTests {

        @Test
        @DisplayName("Deve atualizar senha com sucesso quando email e senha válidos são fornecidos")
        void shouldUpdatePasswordSuccessfully_WhenValidEmailAndPasswordProvided() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String newPassword = "novaSenha123456";
            
            doNothing().when(updateGateway).updatePassword(eq(email), eq(newPassword));

            // Act
            authenticationCommandUseCase.updatePassword(email, newPassword);

            // Assert
            verify(updateGateway, times(1)).updatePassword(email, newPassword);
        }

        @Test
        @DisplayName("Deve propagar exceção quando gateway falha na atualização de senha")
        void shouldPropagateException_WhenGatewayFailsOnPasswordUpdate() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String newPassword = "novaSenha123456";
            RuntimeException expectedError = new RuntimeException("Erro na atualização de senha");
            
            doThrow(expectedError).when(updateGateway).updatePassword(eq(email), eq(newPassword));

            // Act & Assert
            assertThatThrownBy(() -> authenticationCommandUseCase.updatePassword(email, newPassword))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na atualização de senha");
            
            verify(updateGateway, times(1)).updatePassword(email, newPassword);
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
            String password = "senha123456";
            
            // Act & Assert
            for (String email : validEmails) {
                doNothing().when(updateGateway).updatePassword(eq(email), eq(password));
                
                authenticationCommandUseCase.updatePassword(email, password);
                
                verify(updateGateway, times(1)).updatePassword(email, password);
                reset(updateGateway); // Limpar para próxima iteração
            }
        }

        @Test
        @DisplayName("Deve aceitar diferentes senhas válidas")
        void shouldAcceptDifferentValidPasswords() {
            // Arrange
            String email = "joao.silva@hospital.com";
            String[] validPasswords = {
                "senha123456",
                "NovaSenh@2024!",
                "minhaSenhaSegura987",
                "12345678901234567890" // Senha longa
            };
            
            // Act & Assert
            for (String password : validPasswords) {
                doNothing().when(updateGateway).updatePassword(eq(email), eq(password));
                
                authenticationCommandUseCase.updatePassword(email, password);
                
                verify(updateGateway, times(1)).updatePassword(email, password);
                reset(updateGateway); // Limpar para próxima iteração
            }
        }

        @Test
        @DisplayName("Deve chamar gateway mesmo quando email ou senha são nulos")
        void shouldCallGateway_EvenWhenEmailOrPasswordAreNull() {
            // Arrange - Esta responsabilidade de validação pode estar em outras camadas
            String nullEmail = null;
            String nullPassword = null;
            String validEmail = "joao.silva@hospital.com";
            String validPassword = "senha123456";
            
            doNothing().when(updateGateway).updatePassword(any(), any());

            // Act & Assert - Testando diferentes combinações
            authenticationCommandUseCase.updatePassword(nullEmail, validPassword);
            verify(updateGateway, times(1)).updatePassword(nullEmail, validPassword);
            
            authenticationCommandUseCase.updatePassword(validEmail, nullPassword);
            verify(updateGateway, times(1)).updatePassword(validEmail, nullPassword);
            
            authenticationCommandUseCase.updatePassword(nullEmail, nullPassword);
            verify(updateGateway, times(1)).updatePassword(nullEmail, nullPassword);
        }
    }
}
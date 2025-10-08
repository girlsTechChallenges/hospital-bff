package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UserCommandUseCaseImpl
 * 
 * Testa as regras de negócio relacionadas aos comandos de usuário:
 * - Criação de usuários
 * - Atualização de usuários  
 * - Exclusão de usuários
 * 
 * Utiliza mocks para isolar as dependências externas (gateways)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserCommandUseCase - Testes Unitários")
class UserCommandUseCaseImplTest {

    @Mock
    private SaveGateway saveGateway;
    
    @Mock
    private UpdateGateway updateGateway;
    
    @Mock 
    private DeleteGateway deleteGateway;
    
    @InjectMocks
    private UserCommandUseCaseImpl userCommandUseCase;

    @Nested
    @DisplayName("Testes de Criação de Usuário")
    class CreateUserTests {

        @Test
        @DisplayName("Deve criar usuário com sucesso quando dados válidos são fornecidos")
        void shouldCreateUserSuccessfully_WhenValidDataProvided() {
            // Arrange - Preparar dados de teste
            User inputUser = TestDataBuilder.createValidUserDomain();
            User expectedUser = TestDataBuilder.createValidUserDomain();
            
            when(saveGateway.save(any(User.class))).thenReturn(expectedUser);

            // Act - Executar operação
            User actualUser = userCommandUseCase.createUser(inputUser);

            // Assert - Verificar resultados
            assertThat(actualUser).isNotNull();
            assertThat(actualUser.getNome()).isEqualTo(expectedUser.getNome());
            assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
            assertThat(actualUser.getLogin()).isEqualTo(expectedUser.getLogin());
            assertThat(actualUser.getTipo()).isEqualTo(expectedUser.getTipo());
            
            // Verificar que o gateway foi chamado corretamente
            verify(saveGateway, times(1)).save(inputUser);
        }

        @Test
        @DisplayName("Deve propagar exceção quando gateway falha na criação")
        void shouldPropagateException_WhenGatewayFailsOnCreation() {
            // Arrange
            User inputUser = TestDataBuilder.createValidUserDomain();
            RuntimeException expectedError = new RuntimeException("Erro no gateway");
            
            when(saveGateway.save(any(User.class))).thenThrow(expectedError);

            // Act & Assert
            assertThatThrownBy(() -> userCommandUseCase.createUser(inputUser))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no gateway");
            
            verify(saveGateway, times(1)).save(inputUser);
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Usuário")
    class UpdateUserTests {

        @Test
        @DisplayName("Deve atualizar usuário com sucesso quando ID e dados válidos são fornecidos")
        void shouldUpdateUserSuccessfully_WhenValidIdAndDataProvided() {
            // Arrange
            Long userId = 1L;
            User inputUser = TestDataBuilder.createValidUserDomain();
            User expectedUser = TestDataBuilder.createValidUserDomain();
            
            when(updateGateway.update(eq(userId), any(User.class)))
                    .thenReturn(Optional.of(expectedUser));

            // Act
            Optional<User> actualUser = userCommandUseCase.updateUser(userId, inputUser);

            // Assert
            assertThat(actualUser).isPresent();
            assertThat(actualUser.get().getNome()).isEqualTo(expectedUser.getNome());
            assertThat(actualUser.get().getEmail()).isEqualTo(expectedUser.getEmail());
            
            verify(updateGateway, times(1)).update(userId, inputUser);
        }

        @Test
        @DisplayName("Deve retornar Optional.empty quando usuário não existe para atualização")
        void shouldReturnEmptyOptional_WhenUserDoesNotExistForUpdate() {
            // Arrange
            Long nonExistentUserId = 999L;
            User inputUser = TestDataBuilder.createValidUserDomain();
            
            when(updateGateway.update(eq(nonExistentUserId), any(User.class)))
                    .thenReturn(Optional.empty());

            // Act
            Optional<User> actualUser = userCommandUseCase.updateUser(nonExistentUserId, inputUser);

            // Assert
            assertThat(actualUser).isEmpty();
            
            verify(updateGateway, times(1)).update(nonExistentUserId, inputUser);
        }

        @Test
        @DisplayName("Deve propagar exceção quando gateway falha na atualização")
        void shouldPropagateException_WhenGatewayFailsOnUpdate() {
            // Arrange
            Long userId = 1L;
            User inputUser = TestDataBuilder.createValidUserDomain();
            RuntimeException expectedError = new RuntimeException("Erro na atualização");
            
            when(updateGateway.update(eq(userId), any(User.class))).thenThrow(expectedError);

            // Act & Assert
            assertThatThrownBy(() -> userCommandUseCase.updateUser(userId, inputUser))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na atualização");
            
            verify(updateGateway, times(1)).update(userId, inputUser);
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão de Usuário")
    class DeleteUserTests {

        @Test
        @DisplayName("Deve excluir usuário com sucesso quando ID válido é fornecido")
        void shouldDeleteUserSuccessfully_WhenValidIdProvided() {
            // Arrange
            Long userId = 1L;
            User expectedDeletedUser = TestDataBuilder.createValidUserDomain();
            
            when(deleteGateway.deleteById(userId)).thenReturn(Optional.of(expectedDeletedUser));

            // Act
            Optional<User> deletedUser = userCommandUseCase.deleteUser(userId);

            // Assert
            assertThat(deletedUser).isPresent();
            assertThat(deletedUser.get().getNome()).isEqualTo(expectedDeletedUser.getNome());
            assertThat(deletedUser.get().getEmail()).isEqualTo(expectedDeletedUser.getEmail());
            
            verify(deleteGateway, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("Deve retornar Optional.empty quando usuário não existe para exclusão")
        void shouldReturnEmptyOptional_WhenUserDoesNotExistForDeletion() {
            // Arrange
            Long nonExistentUserId = 999L;
            
            when(deleteGateway.deleteById(nonExistentUserId)).thenReturn(Optional.empty());

            // Act
            Optional<User> deletedUser = userCommandUseCase.deleteUser(nonExistentUserId);

            // Assert
            assertThat(deletedUser).isEmpty();
            
            verify(deleteGateway, times(1)).deleteById(nonExistentUserId);
        }

        @Test
        @DisplayName("Deve propagar exceção quando gateway falha na exclusão")
        void shouldPropagateException_WhenGatewayFailsOnDeletion() {
            // Arrange
            Long userId = 1L;
            RuntimeException expectedError = new RuntimeException("Erro na exclusão");
            
            when(deleteGateway.deleteById(userId)).thenThrow(expectedError);

            // Act & Assert
            assertThatThrownBy(() -> userCommandUseCase.deleteUser(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro na exclusão");
            
            verify(deleteGateway, times(1)).deleteById(userId);
        }
    }
}
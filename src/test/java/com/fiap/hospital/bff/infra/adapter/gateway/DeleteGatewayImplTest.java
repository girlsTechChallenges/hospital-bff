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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para DeleteGatewayImpl
 * 
 * Testa as operações de exclusão de usuários:
 * - Exclusão bem-sucedida quando usuário existe
 * - Comportamento quando usuário não existe
 * - Validação de integração com repository e mapper
 * 
 * Utiliza mocks para isolar dependências externas (repository, mapper)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteGateway - Testes Unitários")
class DeleteGatewayImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private DeleteGatewayImpl deleteGateway;

    @Nested
    @DisplayName("Testes de Exclusão de Usuário")
    class DeleteUserTests {

        @Test
        @DisplayName("Deve excluir usuário com sucesso quando ID existe")
        void shouldDeleteUserSuccessfully_WhenIdExists() {
            // Arrange
            Long userId = 1L;
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            userEntity.setId(userId);
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(userEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = deleteGateway.deleteById(userId);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expectedUser);
            
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, times(1)).toUserDomain(userEntity);
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("Deve retornar Optional.empty quando usuário não existe")
        void shouldReturnEmptyOptional_WhenUserDoesNotExist() {
            // Arrange
            Long nonExistentId = 999L;
            
            when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act
            Optional<User> result = deleteGateway.deleteById(nonExistentId);

            // Assert
            assertThat(result).isEmpty();
            
            verify(userRepository, times(1)).findById(nonExistentId);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
            verify(userRepository, never()).deleteById(any(Long.class));
        }

        @Test
        @DisplayName("Deve propagar exceção quando repository falha na busca")
        void shouldPropagateException_WhenRepositoryFailsOnFind() {
            // Arrange
            Long userId = 1L;
            RuntimeException repositoryException = new RuntimeException("Repository error");
            
            when(userRepository.findById(userId)).thenThrow(repositoryException);

            // Act & Assert
            assertThatThrownBy(() -> deleteGateway.deleteById(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Repository error");
            
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
            verify(userRepository, never()).deleteById(any(Long.class));
        }

        @Test
        @DisplayName("Deve propagar exceção quando mapper falha")
        void shouldPropagateException_WhenMapperFails() {
            // Arrange
            Long userId = 1L;
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            RuntimeException mapperException = new RuntimeException("Mapper error");
            
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(userEntity)).thenThrow(mapperException);

            // Act & Assert
            assertThatThrownBy(() -> deleteGateway.deleteById(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Mapper error");
            
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, times(1)).toUserDomain(userEntity);
            verify(userRepository, never()).deleteById(any(Long.class));
        }

        @Test
        @DisplayName("Deve propagar exceção quando repository falha na exclusão")
        void shouldPropagateException_WhenRepositoryFailsOnDelete() {
            // Arrange
            Long userId = 1L;
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            User mappedUser = TestDataBuilder.createValidUserDomain();
            RuntimeException deleteException = new RuntimeException("Delete error");
            
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(userEntity)).thenReturn(mappedUser);
            doThrow(deleteException).when(userRepository).deleteById(userId);

            // Act & Assert
            assertThatThrownBy(() -> deleteGateway.deleteById(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Delete error");
            
            verify(userRepository, times(1)).findById(userId);
            verify(userMapper, times(1)).toUserDomain(userEntity);
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("Deve manter ordem correta de operações: find -> map -> delete")
        void shouldMaintainCorrectOperationOrder_FindMapDelete() {
            // Arrange
            Long userId = 1L;
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(userEntity)).thenReturn(expectedUser);

            // Act
            Optional<User> result = deleteGateway.deleteById(userId);

            // Assert
            assertThat(result).isPresent();
            
            // Verify order of operations using InOrder
            var inOrder = inOrder(userRepository, userMapper);
            inOrder.verify(userRepository).findById(userId);
            inOrder.verify(userMapper).toUserDomain(userEntity);
            inOrder.verify(userRepository).deleteById(userId);
        }

        @Test
        @DisplayName("Deve funcionar com diferentes IDs válidos")
        void shouldWorkWithDifferentValidIds() {
            // Test data
            Long[] testIds = {1L, 5L, 100L, 999L, Long.MAX_VALUE};
            
            for (Long testId : testIds) {
                // Reset mocks for each iteration
                reset(userRepository, userMapper);
                
                // Arrange
                UserEntity userEntity = TestDataBuilder.createValidUserEntity();
                userEntity.setId(testId);
                User expectedUser = TestDataBuilder.createValidUserDomain();

                when(userRepository.findById(testId)).thenReturn(Optional.of(userEntity));
                when(userMapper.toUserDomain(userEntity)).thenReturn(expectedUser);

                // Act
                Optional<User> result = deleteGateway.deleteById(testId);

                // Assert
                assertThat(result).isPresent();
                assertThat(result.get()).isEqualTo(expectedUser);
                
                verify(userRepository, times(1)).findById(testId);
                verify(userRepository, times(1)).deleteById(testId);
                verify(userMapper, times(1)).toUserDomain(userEntity);
            }
        }
    }

    @Nested
    @DisplayName("Testes de Comportamento de Borda")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve tratar ID nulo graciosamente")
        void shouldHandleNullIdGracefully() {
            // Arrange
            when(userRepository.findById(null)).thenReturn(Optional.empty());
            
            // Act
            Optional<User> result = deleteGateway.deleteById(null);
            
            // Assert
            assertThat(result).isEmpty();
            verify(userRepository, times(1)).findById(null);
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
            verify(userRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve tratar ID zero como válido")
        void shouldTreatZeroIdAsValid() {
            // Arrange
            Long zeroId = 0L;
            when(userRepository.findById(zeroId)).thenReturn(Optional.empty());

            // Act
            Optional<User> result = deleteGateway.deleteById(zeroId);

            // Assert
            assertThat(result).isEmpty();
            verify(userRepository, times(1)).findById(zeroId);
        }

        @Test
        @DisplayName("Deve tratar ID negativo como válido")
        void shouldTreatNegativeIdAsValid() {
            // Arrange
            Long negativeId = -1L;
            when(userRepository.findById(negativeId)).thenReturn(Optional.empty());

            // Act
            Optional<User> result = deleteGateway.deleteById(negativeId);

            // Assert
            assertThat(result).isEmpty();
            verify(userRepository, times(1)).findById(negativeId);
        }
    }

    @Nested
    @DisplayName("Testes de Performance e Eficiência")
    class PerformanceTests {

        @Test
        @DisplayName("Deve fazer apenas uma chamada para cada dependência no caso de sucesso")
        void shouldMakeOnlyOneCallToEachDependency_OnSuccess() {
            // Arrange
            Long userId = 1L;
            UserEntity userEntity = TestDataBuilder.createValidUserEntity();
            User expectedUser = TestDataBuilder.createValidUserDomain();

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userMapper.toUserDomain(userEntity)).thenReturn(expectedUser);

            // Act
            deleteGateway.deleteById(userId);

            // Assert - Verify each dependency is called exactly once
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).deleteById(userId);
            verify(userMapper, times(1)).toUserDomain(userEntity);
            
            // Verify no additional interactions
            verifyNoMoreInteractions(userRepository, userMapper);
        }

        @Test
        @DisplayName("Deve otimizar chamadas quando usuário não existe")
        void shouldOptimizeCalls_WhenUserDoesNotExist() {
            // Arrange
            Long nonExistentId = 999L;
            when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act
            deleteGateway.deleteById(nonExistentId);

            // Assert - Should not call expensive operations when user doesn't exist
            verify(userRepository, times(1)).findById(nonExistentId);
            verify(userRepository, never()).deleteById(any(Long.class));
            verify(userMapper, never()).toUserDomain(any(UserEntity.class));
            
            verifyNoMoreInteractions(userRepository, userMapper);
        }
    }
}
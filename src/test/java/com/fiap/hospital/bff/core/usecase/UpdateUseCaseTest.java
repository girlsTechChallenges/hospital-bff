package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UpdateUseCaseTest {

    private UpdateGateway updateGateway;
    private UpdateUseCase updateUseCase;

    @BeforeEach
    void setUp() {
        updateGateway = mock(UpdateGateway.class);
        updateUseCase = new UpdateUseCase(updateGateway);
    }

    @Test
    void testUpdatePasswordDelegatesToGateway() {
        // Arrange
        String email = "test@example.com";
        String password = "newpassword123";

        // Act
        updateUseCase.updatePassword(email, password);

        // Assert
        verify(updateGateway, times(1)).updatePassword(email, password);
    }
}

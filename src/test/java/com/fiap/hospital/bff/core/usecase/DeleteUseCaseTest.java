package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUseCaseTest {

    private DeleteGateway deleteGateway;
    private DeleteUseCase deleteUseCase;

    @BeforeEach
    void setUp() {
        deleteGateway = mock(DeleteGateway.class);
        deleteUseCase = new DeleteUseCase(deleteGateway);
    }

    @Test
    void testDeleteUserFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setType(new Type(1L, "ADMIN", Arrays.asList("ROLE_ADMIN")));

        when(deleteGateway.deleteById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = deleteUseCase.delete(userId);

        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
        verify(deleteGateway).deleteById(userId);
    }

    @Test
    void testDeleteUserNotFound() {
        Long userId = 2L;
        when(deleteGateway.deleteById(userId)).thenReturn(Optional.empty());

        Optional<User> result = deleteUseCase.delete(userId);

        assertFalse(result.isPresent());
        verify(deleteGateway).deleteById(userId);
    }
}

package com.fiap.hospital.bff.infra.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserNotFoundExceptionTest {

    @Test
    void testConstructorWithUserId() {
        Long userId = 123L;
        UserNotFoundException ex = new UserNotFoundException(userId);

        assertEquals("User 123 not found", ex.getMessage());
    }

    @Test
    void testConstructorWithEmail() {
        String email = "test@example.com";
        UserNotFoundException ex = new UserNotFoundException(email);

        assertEquals("User test@example.com not found", ex.getMessage());
    }
}

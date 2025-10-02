package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserCredentialsDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsNotNull_thenNoViolations() {
        UserCredentialsDto dto = new UserCredentialsDto("user@example.com", "password123");
        Set<ConstraintViolation<UserCredentialsDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEmailIsNull_thenViolation() {
        UserCredentialsDto dto = new UserCredentialsDto(null, "password123");
        Set<ConstraintViolation<UserCredentialsDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void whenPasswordIsNull_thenViolation() {
        UserCredentialsDto dto = new UserCredentialsDto("user@example.com", null);
        Set<ConstraintViolation<UserCredentialsDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
}

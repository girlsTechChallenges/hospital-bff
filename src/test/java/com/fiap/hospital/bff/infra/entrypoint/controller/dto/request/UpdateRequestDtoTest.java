package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Type validType() {
        // Exemplo simples de um Type válido. Ajuste conforme sua implementação real.
        return new Type(1L, "DOCTOR", List.of("",""));
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        UpdateRequestDto dto = new UpdateRequestDto(
                "John Doe",
                "john.doe@example.com",
                "StrongPassword123",
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameNull_thenViolation() {
        UpdateRequestDto dto = new UpdateRequestDto(
                null,
                "john.doe@example.com",
                "StrongPassword123",
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void whenNameTooShort_thenViolation() {
        UpdateRequestDto dto = new UpdateRequestDto(
                "J",
                "john.doe@example.com",
                "StrongPassword123",
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void whenNameTooLong_thenViolation() {
        String longName = "J".repeat(51);
        UpdateRequestDto dto = new UpdateRequestDto(
                longName,
                "john.doe@example.com",
                "StrongPassword123",
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void whenEmailInvalid_thenViolation() {
        UpdateRequestDto dto = new UpdateRequestDto(
                "John Doe",
                "invalid-email",
                "StrongPassword123",
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void whenPasswordTooShort_thenViolation() {
        UpdateRequestDto dto = new UpdateRequestDto(
                "John Doe",
                "john.doe@example.com",
                "short",
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void whenPasswordTooLong_thenViolation() {
        String longPassword = "p".repeat(101);
        UpdateRequestDto dto = new UpdateRequestDto(
                "John Doe",
                "john.doe@example.com",
                longPassword,
                validType()
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void whenTypeNull_thenViolation() {
        UpdateRequestDto dto = new UpdateRequestDto(
                "John Doe",
                "john.doe@example.com",
                "StrongPassword123",
                null
        );

        Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }
}

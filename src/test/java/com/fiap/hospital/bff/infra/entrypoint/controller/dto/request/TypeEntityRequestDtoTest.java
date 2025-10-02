package com.fiap.hospital.bff.infra.entrypoint.controller.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TypeEntityRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenNameTypeIsNull_thenValidationFails() {
        TypeEntityRequestDto dto = new TypeEntityRequestDto(null, List.of("ROLE_USER"));

        Set<ConstraintViolation<TypeEntityRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nameType")));
    }

    @Test
    void whenNameTypeIsEmpty_thenValidationFails() {
        TypeEntityRequestDto dto = new TypeEntityRequestDto("", List.of("ROLE_ADMIN"));

        Set<ConstraintViolation<TypeEntityRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nameType")));
    }

    @Test
    void whenNameTypeIsValid_thenValidationPasses() {
        TypeEntityRequestDto dto = new TypeEntityRequestDto("Admin", List.of("ROLE_ADMIN"));

        Set<ConstraintViolation<TypeEntityRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenRolesIsNull_thenValidationPasses() {
        // roles is not annotated, so null is accepted
        TypeEntityRequestDto dto = new TypeEntityRequestDto("User", null);

        Set<ConstraintViolation<TypeEntityRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}

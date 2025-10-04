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

class UserDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDto() {
        UserDto dto = new UserDto(
                "João da Silva",
                "joao.silva@example.com",
                "joaosilva",
                "password123",
                new Type(1L,"DOCTOR", List.of())
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void testInvalidName() {
        UserDto dto = new UserDto(
                "João123",  // contém números, inválido
                "joao.silva@example.com",
                "joaosilva",
                "password123",
                new Type(1L,"DOCTOR", List.of())
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testNullEmail() {
        UserDto dto = new UserDto(
                "João da Silva",
                null,
                "joaosilva",
                "password123",
                new Type(1L,"DOCTOR", List.of())
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testBlankLogin() {
        UserDto dto = new UserDto(
                "João da Silva",
                "joao.silva@example.com",
                " ",  // blank
                "password123",
                new Type(1L,"DOCTOR", List.of())
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void testShortPassword() {
        UserDto dto = new UserDto(
                "João da Silva",
                "joao.silva@example.com",
                "joaosilva",
                "123",  // senha muito curta
                new Type(1L,"DOCTOR", List.of())
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testNullType() {
        UserDto dto = new UserDto(
                "João da Silva",
                "joao.silva@example.com",
                "joaosilva",
                "password123",
                null
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }
}

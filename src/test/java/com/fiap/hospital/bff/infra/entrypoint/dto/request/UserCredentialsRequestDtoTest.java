//package com.fiap.hospital.bff.infra.entrypoint.dto.request;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserCredentialsRequestDtoTest {
//
//    private static Validator validator;
//
//    @BeforeAll
//    static void setupValidator() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    void whenAllFieldsNotNull_thenNoViolations() {
//        UserCredentialsRequestDto dto = new UserCredentialsRequestDto("user@example.com", "password123");
//        Set<ConstraintViolation<UserCredentialsRequestDto>> violations = validator.validate(dto);
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    void whenEmailIsNull_thenViolation() {
//        UserCredentialsRequestDto dto = new UserCredentialsRequestDto(null, "password123");
//        Set<ConstraintViolation<UserCredentialsRequestDto>> violations = validator.validate(dto);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream()
//                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
//    }
//
//    @Test
//    void whenPasswordIsNull_thenViolation() {
//        UserCredentialsRequestDto dto = new UserCredentialsRequestDto("user@example.com", null);
//        Set<ConstraintViolation<UserCredentialsRequestDto>> violations = validator.validate(dto);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream()
//                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
//    }
//}

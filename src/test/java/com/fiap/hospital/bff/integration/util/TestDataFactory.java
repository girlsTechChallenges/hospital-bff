package com.fiap.hospital.bff.integration.util;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.TypeEntityRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserCredentialsDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Test data factory for creating test objects used in integration tests.
 */
public class TestDataFactory {

    // User Test Data
    public static UserDto createValidUserRequest() {
        return new UserDto(
                "João Silva",
                "joao.silva@test.com",
                "joaosilva",
                "password123",
                new Type(1L,"NURSE",List.of("scope1", "scope2"))
        );
    }

    public static UserDto createValidOwnerUserRequest() {
        return new UserDto(
                "Maria Proprietária",
                "maria.owner@test.com",
                "mariaowner",
                "password123",
                new Type(1L,"NURSE",List.of("scope1", "scope2"))
        );
    }

    public static UpdateRequestDto createValidUpdateRequest() {
        return new UpdateRequestDto(
                "João Silva Updated",
                "joao.updated@test.com",
                "password456",
                new Type(1L,"NURSE",List.of("scope1", "scope2"))
        );
    }

    public static UserCredentialsDto createValidCredentials() {
        return new UserCredentialsDto("joao.silva@test.com", "password123");
    }


    // Type User Test Data
    public static TypeEntityRequestDto createValidTypeUserRequest() {
        return new TypeEntityRequestDto("ADMINISTRADOR",List.of());
    }

    // Invalid data for negative tests
    public static UserDto createInvalidUserRequest() {
        return new UserDto(
                "", // Invalid empty name
                "invalid-email", // Invalid email format
                "ab", // Invalid login too short
                "123", // Invalid password too short
                new Type(1L,"NURSE",List.of("scope1", "scope2"))
        );
    }

}

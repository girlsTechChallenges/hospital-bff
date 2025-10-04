package com.fiap.hospital.bff.e2e.util;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.TypeEntityRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserCredentialsDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;

import java.util.List;

/**
 * Factory class for creating test data for E2E tests.
 * Provides realistic test data that can be used in end-to-end scenarios.
 */
public class E2ETestDataFactory {
    /**
     * Creates a valid type user request
     */
    public static TypeEntityRequestDto createValidTypeUserRequest(String typeName) {
        return new TypeEntityRequestDto(typeName ,List.of("scope1","scope2"));
    }

    /**
     * Creates a valid user request with unique data
     */
    public static UserDto createValidUserRequest(String uniqueId) {
        return new UserDto(
                "João Silva " + uniqueId,
                createUniqueEmail(uniqueId),
                "joao.silva." + uniqueId,
                "senhaSegura123",
                new Type(1L,"NURSE",List.of("scope1", "scope2"))
        );
    }
    
    /**
     * Creates a valid doctor user request
     */
    public static UserDto createValidDoctorUserRequest(String uniqueId) {
        return new UserDto(
                "Doctor " + uniqueId,
                createUniqueEmail(uniqueId),
                "Doctor." + uniqueId,
                "doctorPass123",
                new Type(1L,"DOCTOR",List.of("scope1", "scope2"))
        );
    }

    /**
     * Creates valid user credentials for authentication
     */
    public static UserCredentialsDto createValidCredentials(String email, String password) {
        return new UserCredentialsDto(email, password);
    }
    
    /**
     * Creates authentication request for testing
     */
    public static UserCredentialsDto createValidAuthRequest(String uniqueId) {
        return new UserCredentialsDto(
                createUniqueEmail(uniqueId),
                "senha123456"
        );
    }

    /**
     * Creates an update request for users
     */
    public static UpdateRequestDto createUpdateUserRequest(String uniqueId) {
        return new UpdateRequestDto(
                "Nome Atualizado " + uniqueId,
                createUniqueEmail(uniqueId),
                "novaSenha123",
                new Type(1L,"DOCTOR", List.of("scope1","scope2"))
        );
    }

    /**
     * Creates a unique email address for testing
     */
    private static String createUniqueEmail(String uniqueId) {
        return "e2e.test." + uniqueId + "@hospital-bff.com";
    }
    
    /**
     * Creates invalid data for negative testing scenarios
     */
    public static class InvalidData {

        public static TypeEntityRequestDto createInvalidTypeUserRequest() {
            return new TypeEntityRequestDto("", List.of()); // Empty type name
        }

        public static UserDto createInvalidUserRequest() {
            return new UserDto(
                    "", // Nome vazio
                    "email-invalido", // Email inválido
                    "a", // Login muito curto
                    "123", // Senha muito curta
                    new Type() // Tipo vazio
            );
        }
    }
}

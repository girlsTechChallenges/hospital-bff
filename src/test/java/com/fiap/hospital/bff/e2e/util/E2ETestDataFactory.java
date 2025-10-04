//package com.fiap.hospital.bff.e2e.util;
//
//import com.fiap.hospital.bff.core.domain.model.user.Type;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeRequestDto;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.UpdateRequestDto;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserCredentialsRequestDto;
//import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
//
//import java.util.List;
//
///**
// * Factory class for creating test data for E2E tests.
// * Provides realistic test data that can be used in end-to-end scenarios.
// */
//public class E2ETestDataFactory {
//    /**
//     * Creates a valid type user request
//     */
//    public static TypeRequestDto createValidTypeUserRequest(String typeName) {
//        return new TypeRequestDto(typeName ,List.of("scope1","scope2"));
//    }
//
//    /**
//     * Creates a valid user request with unique data
//     */
//    public static UserRequestDto createValidUserRequest(String uniqueId) {
//        return new UserRequestDto(
//                "João Silva " + uniqueId,
//                createUniqueEmail(uniqueId),
//                "joao.silva." + uniqueId,
//                "senhaSegura123",
//                new Type(1L,"NURSE",List.of("scope1", "scope2"))
//        );
//    }
//
//    /**
//     * Creates a valid doctor user request
//     */
//    public static UserRequestDto createValidDoctorUserRequest(String uniqueId) {
//        return new UserRequestDto(
//                "Doctor " + uniqueId,
//                createUniqueEmail(uniqueId),
//                "Doctor." + uniqueId,
//                "doctorPass123",
//                new Type(1L,"DOCTOR",List.of("scope1", "scope2"))
//        );
//    }
//
//    /**
//     * Creates valid user credentials for authentication
//     */
//    public static UserCredentialsRequestDto createValidCredentials(String email, String password) {
//        return new UserCredentialsRequestDto(email, password);
//    }
//
//    /**
//     * Creates authentication request for testing
//     */
//    public static UserCredentialsRequestDto createValidAuthRequest(String uniqueId) {
//        return new UserCredentialsRequestDto(
//                createUniqueEmail(uniqueId),
//                "senha123456"
//        );
//    }
//
//    /**
//     * Creates an update request for users
//     */
//    public static UpdateRequestDto createUpdateUserRequest(String uniqueId) {
//        return new UpdateRequestDto(
//                "Nome Atualizado " + uniqueId,
//                createUniqueEmail(uniqueId),
//                "novaSenha123",
//                new Type(1L,"DOCTOR", List.of("scope1","scope2"))
//        );
//    }
//
//    /**
//     * Creates a unique email address for testing
//     */
//    private static String createUniqueEmail(String uniqueId) {
//        return "e2e.test." + uniqueId + "@hospital-bff.com";
//    }
//
//    /**
//     * Creates invalid data for negative testing scenarios
//     */
//    public static class InvalidData {
//
//        public static TypeRequestDto createInvalidTypeUserRequest() {
//            return new TypeRequestDto("", List.of()); // Empty type name
//        }
//
//        public static UserRequestDto createInvalidUserRequest() {
//            return new UserRequestDto(
//                    "", // Nome vazio
//                    "email-invalido", // Email inválido
//                    "a", // Login muito curto
//                    "123", // Senha muito curta
//                    new Type() // Tipo vazio
//            );
//        }
//    }
//}

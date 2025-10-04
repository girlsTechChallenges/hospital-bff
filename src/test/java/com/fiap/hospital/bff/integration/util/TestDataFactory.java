//package com.fiap.hospital.bff.integration.util;
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
// * Test data factory for creating test objects used in integration tests.
// */
//public class TestDataFactory {
//
//    // User Test Data
//    public static UserRequestDto createValidUserRequest() {
//        return new UserRequestDto(
//                "João Silva",
//                "joao.silva@test.com",
//                "joaosilva",
//                "password123",
//                new Type(1L,"NURSE",List.of("scope1", "scope2"))
//        );
//    }
//
//    public static UserRequestDto createValidOwnerUserRequest() {
//        return new UserRequestDto(
//                "Maria Proprietária",
//                "maria.owner@test.com",
//                "mariaowner",
//                "password123",
//                new Type(1L,"NURSE",List.of("scope1", "scope2"))
//        );
//    }
//
//    public static UpdateRequestDto createValidUpdateRequest() {
//        return new UpdateRequestDto(
//                "João Silva Updated",
//                "joao.updated@test.com",
//                "password456",
//                new Type(1L,"NURSE",List.of("scope1", "scope2"))
//        );
//    }
//
//    public static UserCredentialsRequestDto createValidCredentials() {
//        return new UserCredentialsRequestDto("joao.silva@test.com", "password123");
//    }
//
//
//    // Type User Test Data
//    public static TypeRequestDto createValidTypeUserRequest() {
//        return new TypeRequestDto("ADMINISTRADOR",List.of());
//    }
//
//    // Invalid data for negative tests
//    public static UserRequestDto createInvalidUserRequest() {
//        return new UserRequestDto(
//                "", // Invalid empty name
//                "invalid-email", // Invalid email format
//                "ab", // Invalid login too short
//                "123", // Invalid password too short
//                new Type(1L,"NURSE",List.of("scope1", "scope2"))
//        );
//    }
//
//}

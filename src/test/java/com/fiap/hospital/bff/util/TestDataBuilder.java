package com.fiap.hospital.bff.util;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserCredentialsRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultResponseDto;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;

import java.time.LocalDate;

/**
 * Classe utilitária para criação de objetos de teste
 * Centraliza a criação de dados de teste para evitar duplicação
 */
public class TestDataBuilder {

    // ===== USER DATA BUILDERS =====

    public static User createValidUserDomain() {
        return new User(
            "João Silva",
            "joao.silva@hospital.com",
            "joaosilva",
            "senha123456",
            "PACIENTE"
        );
    }

    public static User createValidDoctorDomain() {
        return new User(
            "Dr. Maria Santos",
            "maria.santos@hospital.com",
            "drmaria",
            "senha123456",
            "MEDICO"
        );
    }

    public static UserRequestDto createValidUserRequestDto() {
        return new UserRequestDto(
            "João Silva",
            "joao.silva@hospital.com",
            "joaosilva",
            "senha123456",
            TypeUsers.PACIENTE
        );
    }

    public static UserRequestDto createValidDoctorRequestDto() {
        return new UserRequestDto(
            "Dr. Maria Santos",
            "maria.santos@hospital.com",
            "drmaria",
            "senha123456",
            TypeUsers.MEDICO
        );
    }

    public static UserEntity createValidUserEntity() {
        return UserEntity.builder()
            .id(1L)
            .nome("João Silva")
            .email("joao.silva@hospital.com")
            .login("joaosilva")
            .senha("$2a$10$hashedPassword")
            .tipo(TypeUsers.PACIENTE)
            .build();
    }

    public static UserCredentialsRequestDto createValidCredentials() {
        return new UserCredentialsRequestDto(
            "joao.silva@hospital.com",
            "senha123456"
        );
    }

    public static UserCredentialsRequestDto createInvalidCredentials() {
        return new UserCredentialsRequestDto(
            "joao.silva@hospital.com",
            "senhaErrada"
        );
    }

    // ===== CONSULT DATA BUILDERS =====

    public static ConsultRequestDto createValidConsultRequestDto() {
        return new ConsultRequestDto(
            "João Silva",
            "joao.silva@hospital.com",
            "maria.santos@hospital.com",
            LocalDate.now().plusDays(7),
            "Consulta de rotina"
        );
    }

    public static ConsultResponseDto createValidConsultResponseDto() {
        return new ConsultResponseDto(
            1L,
            123L,
            456L,
            "João Silva",
            "joao.silva@hospital.com",
            "Dr. Maria Santos",
            LocalDate.now().plusDays(7),
            "Consulta de rotina",
            "AGENDADA"
        );
    }

    // ===== INVALID DATA BUILDERS =====

    public static UserRequestDto createInvalidUserRequestDto() {
        return new UserRequestDto(
            "", // Nome vazio - inválido
            "email-invalido", // Email inválido
            "ab", // Login muito curto
            "123", // Senha muito curta
            TypeUsers.PACIENTE
        );
    }

    public static ConsultRequestDto createInvalidConsultRequestDto() {
        return new ConsultRequestDto(
            "", // Nome vazio
            "email-invalido", // Email inválido
            "", // Email profissional vazio
            LocalDate.now().minusDays(1), // Data no passado
            "" // Motivo vazio
        );
    }

    // ===== EMAIL TEMPLATES =====

    public static String createUniqueEmail() {
        return "user" + System.currentTimeMillis() + "@hospital.com";
    }

    public static String createUniqueLogin() {
        return "login" + System.currentTimeMillis();
    }
}
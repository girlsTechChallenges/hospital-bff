package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.TypeResponseDto;
import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.entity.UserTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class TypeMapper {

    public Type toDomain(TypeRequestDto dto) {
        if (dto == null) return null;

        return new Type(
                null,
                dto.nameType().trim().toUpperCase(),
                dto.roles()
        );
    }

    public Type toDomain(TypeEntity entity) {
        if (entity == null) return null;

        List<String> roles = entity.getRoles() != null
                ? entity.getRoles().stream().toList()
                : List.of();

        return new Type(
                entity.getId(),
                entity.getNameType(),
                roles
        );
    }

    public TypeEntity toEntity(Type type) {
        if (type == null) return null;

        Set<String> roles = type.getRoles() != null
                ? Set.copyOf(type.getRoles())
                : Set.of();

        UserTypeEnum userType = determineUserTypeEnum(type.getNameType());

        return TypeEntity.builder()
                .id(type.getId())
                .nameType(type.getNameType().trim().toUpperCase())
                .userType(userType)
                .roles(roles)
                .build();
    }

    public TypeResponseDto toResponseDto(Type type) {
        if (type == null) return null;

        return new TypeResponseDto(
                type.getId(),
                type.getNameType(),
                type.getRoles()
        );
    }

    public TypeResponseDto toResponseDto(Optional<Type> optionalType) {
        return optionalType.map(this::toResponseDto).orElse(null);
    }

    private UserTypeEnum determineUserTypeEnum(String nameType) {
        if (nameType == null) return UserTypeEnum.PATIENT;

        String normalized = nameType.trim().toUpperCase();

        return switch (normalized) {
            case "DOCTOR", "MÉDICO" -> UserTypeEnum.DOCTOR;
            case "NURSE", "ENFERMEIRO", "ENFERMEIRA" -> UserTypeEnum.NURSE;
            case "PATIENT", "PACIENTE" -> UserTypeEnum.PATIENT;
            default -> UserTypeEnum.PATIENT;
        };
    }
}
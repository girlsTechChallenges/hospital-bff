package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.TypeEntityRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.TypeEntityResponse;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TypeEntityMapper {

    public TypeEntity toTypeEntityDomain(TypeEntityRequestDto typeUserRequestDto ) {
        return new TypeEntity(null, typeUserRequestDto.type(), typeUserRequestDto.roles());
    }

    public TypeEntity toTypeEntity(TypeEntity user) {
        return new TypeEntity(null, user.getName(), user.getRoles());
    }

    public TypeEntity toTypeEntityEntity(TypeEntity typeEntity) {
        return new TypeEntity(typeEntity.getId(), typeEntity.getName(), typeEntity.getRoles());
    }

    public TypeEntityResponse typeUserResponse(TypeEntity typeUser) {
        return new TypeEntityResponse(typeUser.getId(), typeUser.getName(), typeUser.getRoles());
    }

    public TypeEntityResponse typeUserResponse(Optional<TypeEntity> response) {
        return response.map(this::typeUserResponse)
                .orElseThrow(() -> new IllegalArgumentException("TypeEntity not found"));
    }
}

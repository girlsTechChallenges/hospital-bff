package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.TypeEntityRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.TypeEntityResponse;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.fiap.hospital.bff.infra.common.MessageConstants.USER_NOT_FOUND;

@Component
public class TypeEntityMapper {

    public Type toTypeEntityDomain(TypeEntityRequestDto typeRequestDto ) {
        return new Type(typeRequestDto.type(), typeRequestDto.roles());
    }

    public Type toTypeDomain(TypeEntityRequestDto typeEntityRequestDto) {
        return new Type(typeEntityRequestDto.type(), typeEntityRequestDto.roles());
    }

    public TypeEntity toTypeEntity(Type type) {
        return new TypeEntity(null, type.getNameType(), type.getRoles());
    }

    public Type toTypeEntityDomain(TypeEntity typeEntity) {
        return new Type(typeEntity.getNameType(), typeEntity.getRoles());
    }

    public TypeEntityResponse typeTypeResponse(Type typeUser) {
        return new TypeEntityResponse(typeUser.getNameType(), typeUser.getRoles());
    }

    public TypeEntityResponse getTypeByIdToTypeResponseDto(Optional<Type> optionalType) {

        Type type = optionalType.orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        return new TypeEntityResponse(type.getNameType(), type.getRoles());
    }

    public TypeEntityResponse toTypeResponseDto(Type type) {
        return new TypeEntityResponse(type.getNameType(), type.getRoles());
    }
}

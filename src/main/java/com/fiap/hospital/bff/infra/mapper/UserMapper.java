package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserAuthRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserMapper {

    // Mapeia DTO para domínio
    public User toDomain(UserRequestDto dto, Type type) {
        if (dto == null || type == null) return null;

        return new User(
                null,
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                LocalDate.now(),
                type
        );
    }

    // Mapeia domínio para entidade com TypeEntity resolvido
    public UserEntity toEntity(User user, TypeEntity typeEntity) {
        if (user == null || typeEntity == null) return null;

        LocalDateTime changeDateTime = user.getChangeDate() != null
                ? user.getChangeDate().atStartOfDay()
                : LocalDateTime.now();

        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .password(user.getPassword())
                .changeDate(changeDateTime)
                .type(typeEntity)
                .build();
    }

    // Mapeia entidade para domínio
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        Type type = null;
        if (entity.getType() != null) {
            List<String> roles = entity.getType().getRoles() != null
                    ? entity.getType().getRoles().stream().toList()
                    : List.of();

            type = new Type(
                    entity.getType().getId(),
                    entity.getType().getNameType(),
                    roles
            );
        }

        LocalDate changeDate = entity.getChangeDate() != null
                ? entity.getChangeDate().toLocalDate()
                : null;

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getPassword(),
                changeDate,
                type
        );
    }

    // Mapeia domínio para resposta DTO
    public UserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        String typeName = user.getType() != null ? user.getType().getNameType() : null;

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                typeName
        );
    }

    public UserResponseDto toResponseDto(Optional<User> optionalUser) {
        return optionalUser.map(this::toResponseDto).orElse(null);
    }

    // Mapeia Token para DTO de autenticação
    public UserAuthRequestDto toAuthDto(Token token) {
        if (token == null) return null;

        return new UserAuthRequestDto(
                token.getAccessToken(),
                token.getExpiresIn(),
                token.getScopes()
        );
    }

    // Atualização parcial
    public User toDomain(UpdateRequestDto dto, Type type) {
        if (dto == null || type == null) return null;

        return new User(
                dto.name(),
                dto.email(),
                dto.password(),
                type
        );
    }
}
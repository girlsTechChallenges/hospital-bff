package com.fiap.hospital.bff.infra.entrypoint.mapper;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserAuthRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUserDomain(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.nome(),
                userRequestDto.email(),
                userRequestDto.login(),
                userRequestDto.senha(),
                userRequestDto.tipo().name()
        );
    }

    public User toUserDomainUpdate(UserUpdateRequestDto updateRequestDto) {
        return new User(
                null,
                updateRequestDto.email(),
                updateRequestDto.login(),
                updateRequestDto.senha(),
                null
        );
    }

    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .login(user.getLogin())
                .senha(user.getSenha())
                .tipo(TypeUsers.valueOf(user.getTipo()))
                .build();
    }

    public User toUserDomain(UserEntity userEntity) {
        return new User(
                userEntity.getNome(),
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getSenha(),
                userEntity.getTipo().name()
        );
    }

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                null,
                user.getNome(),
                user.getEmail(),
                user.getLogin(),
                user.getTipo()
        );
    }

    public UserAuthRequestDto toTokenResponseDto(Token token) {
        return new UserAuthRequestDto(token.getAccessToken(), token.getExpiresIn());
    }
}

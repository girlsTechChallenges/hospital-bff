package com.fiap.hospital.infra.entrypoint.controller.mapper;


import com.fiap.hospital.infra.domain.*;
import com.fiap.hospital.infra.entrypoint.controller.dto.request.*;
import com.fiap.hospital.infra.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEntityMapper {

    public User toUserDomain(UserRequestDto userRequestDto) {

        return new User(
                userRequestDto.nome(),
                userRequestDto.email(),
                userRequestDto.login(),
                userRequestDto.senha(),
                userRequestDto.dataAlteracao(),
                TypeEnum.valueOf(userRequestDto.tipo().name())
        );
    }

    public User updateToUserDomain(UpdateRequestDto updateRequestDto) {

        return new User(
                updateRequestDto.nome(),
                updateRequestDto.email(),
                updateRequestDto.senha(),
                TypeEnum.valueOf(updateRequestDto.tipo().name())
        );
    }

    public UserEntity toUserEntity(User user) {

        return new UserEntity(
                null, // Assuming ID is auto-generated
                user.getNome(),
                user.getEmail(),
                user.getLogin(),
                user.getSenha(),
                user.getDataAlteracao(),
                TypeEntityEnum.valueOf(user.getTipo().name())
                );
    }

    public User toUserDomain(UserEntity userEntity) {

        return new User(
                userEntity.getNome(),
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getSenha(),
                userEntity.getDataAlteracao(),
                TypeEnum.valueOf(userEntity.getTipo().name())
                );
    }

    public UserResponseDto toUserResponseDto(User user) {

        return new UserResponseDto(user.getNome(), user.getLogin(), user.getEmail(), user.getTipo());
    }

    public UserResponseDto getUserByIdToUserResponseDto(Optional<User> optionalUser) {
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        User user = optionalUser.get();

        return new UserResponseDto(user.getNome(), user.getLogin(), user.getEmail(), user.getTipo());
    }

    public UserAuthDto toTokenResponseDto(Token token) {
        return new UserAuthDto(token.getAccessToken(), token.getExpiresIn());
    }

}
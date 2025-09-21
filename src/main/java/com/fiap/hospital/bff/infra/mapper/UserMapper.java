package com.fiap.hospital.bff.infra.mapper;

import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserAuthDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;

@Component
public class UserMapper {

    // private final TypeUserMapper typeUserMapper;

    // public UserMapper(TypeUserMapper typeUserMapper) {
    //     this.typeUserMapper = typeUserMapper;
    // }

    public User toUserDomain(UserDto userRequestDto) {

        return new User(
                userRequestDto.nome(),
                userRequestDto.email(),
                userRequestDto.login(),
                userRequestDto.senha(),
                userRequestDto.dataAlteracao(),
                userRequestDto.tipo());
    }

    // public User updateToUserDomain(UpdateRequestDto updateRequestDto) {

    //     return new User(
    //             updateRequestDto.nome(),
    //             updateRequestDto.email(),
    //             updateRequestDto.senha(),
    //             updateRequestDto.tipo());
    // }

    public UserEntity toUserEntity(User user) {

        return new UserEntity.Builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .login(user.getLogin())
                .senha(user.getSenha())
                .dataAlteracao(user.getDataAlteracao())
                // .tipo(typeUserMapper.toTypeEntity(new TypeUser(null, user.getTipo())))
                .build();
    }

    public User toUserDomain(UserEntity userEntity) {
        return new User(
                userEntity.getNome(),
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getSenha(),
                userEntity.getDataAlteracao(),
                ""); // Providing empty string for tipo since it's currently commented out in UserEntity
    }

    // public UserResponseDto toUserResponseDto(User user) {

    //     return new UserResponseDto(user.getNome(), user.getLogin(), user.getEmail(), user.getTipo(), addressDtos);
    // }

    // public UserResponseDto getUserByIdToUserResponseDto(Optional<User> optionalUser) {

    //     User user = optionalUser.orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

       
    //     return new UserResponseDto(user.getNome(), user.getLogin(), user.getEmail(), user.getTipo());
    // }



    public UserAuthDto toTokenResponseDto(Token token) {
        return new UserAuthDto(token.getAccessToken(), token.getExpiresIn());
    }

}

package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserAuthDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;

import java.util.Optional;

import static com.fiap.hospital.bff.infra.common.MessageConstants.USER_NOT_FOUND;

@Component
public class UserMapper {

    private final TypeEntityMapper typeEntityMapper;

    public UserMapper(TypeEntityMapper typeEntityMapper) {
        this.typeEntityMapper = typeEntityMapper;
    }


    public User toUserDomain(UserDto userRequestDto) {

        return new User(
                userRequestDto.name(),
                userRequestDto.email(),
                userRequestDto.login(),
                userRequestDto.password(),
                userRequestDto.changeDate(),
                userRequestDto.type());
    }

     public User updateUserDomain(UpdateRequestDto updateRequestDto) {

         return new User(
                 updateRequestDto.name(),
                 updateRequestDto.email(),
                 updateRequestDto.password(),
                 updateRequestDto.type());
     }

    public UserEntity toUserEntity(User user) {

        return new UserEntity.Builder()
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .password(user.getPassword())
                .changeDate(user.getChangeDate())
                .type(typeEntityMapper.toTypeEntity(new TypeEntity(null, user.getType())))
                .build();
    }

    public User toUserDomain(UserEntity userEntity) {
        return new User(
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getChangeDate(),
                userEntity.getType().getName()
        );
    }

     public UserResponseDto toUserResponseDto(User user) {

         return new UserResponseDto(user.getName(), user.getLogin(), user.getEmail(), user.getType());
     }

     public UserResponseDto getUserByIdToUserResponseDto(Optional<User> optionalUser) {

         User user = optionalUser.orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

       
         return new UserResponseDto(user.getName(), user.getLogin(), user.getEmail(), user.getType());
     }



    public UserAuthDto toTokenResponseDto(Token token) {
        return new UserAuthDto(token.getAccessToken(), token.getExpiresIn());
    }

}

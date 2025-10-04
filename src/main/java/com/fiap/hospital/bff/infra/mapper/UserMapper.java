package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import org.springframework.stereotype.Component;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserAuthDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;

import java.time.LocalDate;
import java.util.Optional;

import static com.fiap.hospital.bff.infra.common.MessageConstants.USER_NOT_FOUND;

@Component
public class UserMapper {


    public User toUserDomain(UserDto userRequestDto) {

        return new User(
                null,
                userRequestDto.name(),
                userRequestDto.email(),
                userRequestDto.login(),
                userRequestDto.password(),
                LocalDate.now(),
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
        TypeEntity type = new TypeEntity();
        type.setNameType(user.getType().getNameType());
        type.setRoles(user.getType().getRoles());

        return new UserEntity(
                null,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),
                user.getChangeDate(),
                type
        );
    }

    public User toUserDomain(UserEntity userEntity) {

        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getChangeDate(),
                new Type(userEntity.getTypes().getId(), userEntity.getTypes().getNameType(), userEntity.getTypes().getRoles())
        );
    }

     public UserResponseDto toUserResponseDto(User user) {

         return new UserResponseDto(user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getType().getNameType());
     }

     public UserResponseDto getUserByIdToUserResponseDto(Optional<User> optionalUser) {

         User user = optionalUser.orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

       
         return new UserResponseDto(user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getType().getNameType());
     }



    public UserAuthDto toTokenResponseDto(Token token) {
        return new UserAuthDto(token.getAccessToken(), token.getExpiresIn(), token.getScopes());
    }

}

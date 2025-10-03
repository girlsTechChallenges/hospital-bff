package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserAuthDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.fiap.hospital.bff.infra.common.MessageConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private TypeEntityMapper typeEntityMapper;

    @BeforeEach
    void setUp() {
        typeEntityMapper = new TypeEntityMapper();
        userMapper = new UserMapper(typeEntityMapper);
    }

    @Test
    void toUserDomain_FromUserDto_ShouldMapCorrectly() {
        UserDto dto = new UserDto(
                "John Doe",
                "john@example.com",
                "johndoe",
                "password123",
                new Type(null, "Admin", List.of("ROLE_ADMIN")));

        User user = userMapper.toUserDomain(dto);

        assertNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("johndoe", user.getLogin());
        assertEquals("password123", user.getPassword());
        assertEquals(LocalDate.of(2025, 10, 1), user.getChangeDate());
        assertEquals("Admin", user.getType().getNameType());
        assertEquals(List.of("ROLE_ADMIN"), user.getType().getRoles());
    }

    @Test
    void updateUserDomain_FromUpdateRequestDto_ShouldMapCorrectly() {
        UpdateRequestDto dto = new UpdateRequestDto(
                "Jane Doe",
                "jane@example.com",
                "newpassword123",
                new Type(null, "User", List.of("ROLE_USER")));

        User user = userMapper.updateUserDomain(dto);

        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newpassword123", user.getPassword());
        assertEquals("User", user.getType().getNameType());
        assertEquals(List.of("ROLE_USER"), user.getType().getRoles());
    }

    @Test
    void toUserEntity_FromUser_ShouldMapCorrectly() {
        User user = new User(
                1L,
                "John Doe",
                "john@example.com",
                "johndoe",
                "password123",
                LocalDate.now(),
                new Type(2L, "Admin", List.of("ROLE_ADMIN")));

        UserEntity entity = userMapper.toUserEntity(user);

        assertNull(entity.getId()); // the method sets null on id
        assertEquals("John Doe", entity.getName());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("johndoe", entity.getLogin());
        assertEquals("password123", entity.getPassword());
        assertEquals(user.getChangeDate(), entity.getChangeDate());

        TypeEntity typeEntity = entity.getTypes();
        assertEquals("Admin", typeEntity.getNameType());
        assertEquals(List.of("ROLE_ADMIN"), typeEntity.getRoles());
    }

    @Test
    void toUserDomain_FromUserEntity_ShouldMapCorrectly() {
        TypeEntity typeEntity = new TypeEntity(2L, "User", List.of("ROLE_USER"));
        UserEntity userEntity = new UserEntity(
                1L,
                "Jane Doe",
                "jane@example.com",
                "janedoe",
                "password321",
                LocalDate.of(2025, 9, 30),
                typeEntity);

        User user = userMapper.toUserDomain(userEntity);

        assertEquals(1L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("janedoe", user.getLogin());
        assertEquals("password321", user.getPassword());
        assertEquals(LocalDate.of(2025, 9, 30), user.getChangeDate());

        Type type = user.getType();
        assertEquals(2L, type.getId());
        assertEquals("User", type.getNameType());
        assertEquals(List.of("ROLE_USER"), type.getRoles());
    }

    @Test
    void toUserResponseDto_ShouldMapCorrectly() {
        User user = new User(
                10L,
                "Alice",
                "alice@example.com",
                "alice123",
                "secret",
                null,
                new Type(5L, "Manager", List.of("ROLE_MANAGER")));

        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        assertEquals(10L, responseDto.id());
        assertEquals("Alice", responseDto.name());
        assertEquals("alice123", responseDto.login());
        assertEquals("alice@example.com", responseDto.email());
        assertEquals("Manager", responseDto.type());
    }

    @Test
    void getUserByIdToUserResponseDto_Present_ShouldReturnResponseDto() {
        User user = new User(
                20L,
                "Bob",
                "bob@example.com",
                "bob123",
                "pwd",
                null,
                new Type(3L, "Guest", List.of("ROLE_GUEST")));

        Optional<User> optionalUser = Optional.of(user);

        UserResponseDto responseDto = userMapper.getUserByIdToUserResponseDto(optionalUser);

        assertEquals(20L, responseDto.id());
        assertEquals("Bob", responseDto.name());
        assertEquals("bob123", responseDto.login());
        assertEquals("bob@example.com", responseDto.email());
        assertEquals("Guest", responseDto.type());
    }

    @Test
    void getUserByIdToUserResponseDto_Empty_ShouldThrowRuntimeException() {
        Optional<User> emptyUser = Optional.empty();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userMapper.getUserByIdToUserResponseDto(emptyUser);
        });

        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void toTokenResponseDto_ShouldMapCorrectly() {
        Token token = new Token("token123", 3600L);

        UserAuthDto authDto = userMapper.toTokenResponseDto(token);

        assertEquals("token123", authDto.accessToken());
        assertEquals(3600L, authDto.expiresIn());
    }
}

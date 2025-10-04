package com.fiap.hospital.bff.infra.mapper;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserAuthDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.persistence.user.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void shouldMapUserDtoToUserDomain() {
        UserDto userDto = new UserDto("João", "joao@email.com", "joaologin", "123456", new Type(null, "ADMIN", List.of("READ", "WRITE")));

        User user = userMapper.toUserDomain(userDto);

        assertThat(user.getName()).isEqualTo("João");
        assertThat(user.getEmail()).isEqualTo("joao@email.com");
        assertThat(user.getLogin()).isEqualTo("joaologin");
        assertThat(user.getPassword()).isEqualTo("123456");
        assertThat(user.getType().getNameType()).isEqualTo("ADMIN");
        assertThat(user.getType().getRoles()).contains("READ", "WRITE");
    }

    @Test
    void shouldMapUpdateRequestDtoToUserDomain() {
        UpdateRequestDto dto = new UpdateRequestDto("Maria", "maria@email.com", "senhaNova", new Type(null, "USER", List.of("READ")));

        User user = userMapper.updateUserDomain(dto);

        assertThat(user.getName()).isEqualTo("Maria");
        assertThat(user.getEmail()).isEqualTo("maria@email.com");
        assertThat(user.getPassword()).isEqualTo("senhaNova");
        assertThat(user.getType().getNameType()).isEqualTo("USER");
    }

    @Test
    void shouldMapUserDomainToUserEntity() {
        Type type = new Type(null, "ADMIN", List.of("READ", "WRITE"));
        User user = new User(null, "João", "joao@email.com", "joaologin", "senha", LocalDate.now(), type);

        UserEntity entity = userMapper.toUserEntity(user);

        assertThat(entity.getName()).isEqualTo("João");
        assertThat(entity.getTypes().getNameType()).isEqualTo("ADMIN");
        assertThat(entity.getTypes().getRoles()).contains("READ", "WRITE");
    }

    @Test
    void shouldMapUserEntityToUserDomain() {
        TypeEntity typeEntity = new TypeEntity(null, "ADMIN", List.of("READ", "WRITE"));
        UserEntity userEntity = new UserEntity(1L, "Ana", "ana@email.com", "ana123", "senha", LocalDate.now(), typeEntity);

        User user = userMapper.toUserDomain(userEntity);

        assertThat(user.getName()).isEqualTo("Ana");
        assertThat(user.getType().getNameType()).isEqualTo("ADMIN");
        assertThat(user.getType().getRoles()).contains("READ", "WRITE");
    }

    @Test
    void shouldMapUserToUserResponseDto() {
        User user = new User(1L, "Carlos", "carlos@email.com", "carlos123", "senha", LocalDate.now(), new Type(null, "MOD", List.of("VIEW")));

        UserResponseDto dto = userMapper.toUserResponseDto(user);

        assertThat(dto.name()).isEqualTo("Carlos");
        assertThat(dto.email()).isEqualTo("carlos@email.com");
        assertThat(dto.type()).isEqualTo("MOD");
    }

    @Test
    void shouldMapOptionalUserToUserResponseDto() {
        User user = new User(1L, "Luana", "luana@email.com", "luana123", "senha", LocalDate.now(), new Type(null, "USER", List.of("READ")));

        UserResponseDto dto = userMapper.getUserByIdToUserResponseDto(Optional.of(user));

        assertThat(dto.name()).isEqualTo("Luana");
        assertThat(dto.type()).isEqualTo("USER");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        assertThatThrownBy(() -> userMapper.getUserByIdToUserResponseDto(Optional.empty()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id"); // Assuming it's from MessageConstants.USER_NOT_FOUND
    }

    @Test
    void shouldMapTokenToUserAuthDto() {
        Token token = new Token("abc123", 300L, List.of("READ", "WRITE"));

        UserAuthDto authDto = userMapper.toTokenResponseDto(token);

        assertThat(authDto.accessToken()).isEqualTo("abc123");
        assertThat(authDto.expiresIn()).isEqualTo(300L);
        assertThat(authDto.scopes()).contains("READ", "WRITE");
    }
}

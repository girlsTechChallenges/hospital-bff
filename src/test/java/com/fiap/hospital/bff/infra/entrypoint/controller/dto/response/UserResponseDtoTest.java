package com.fiap.hospital.bff.infra.entrypoint.controller.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDtoTest {

    @Test
    void testCreateAndAccessors() {
        Long id = 10L;
        String name = "John Doe";
        String login = "johndoe";
        String email = "john.doe@example.com";
        String type = "ADMIN";

        UserResponseDto userResponseDto = new UserResponseDto(id, name, login, email, type);

        assertEquals(id, userResponseDto.id());
        assertEquals(name, userResponseDto.name());
        assertEquals(login, userResponseDto.login());
        assertEquals(email, userResponseDto.email());
        assertEquals(type, userResponseDto.type());
    }
}

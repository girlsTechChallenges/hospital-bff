package com.fiap.hospital.bff.infra.entrypoint.controller.dto.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeEntityResponseTest {

    @Test
    void testCreateAndAccessors() {
        Long id = 1L;
        String nameType = "Admin";
        List<String> roles = List.of("ROLE_ADMIN", "ROLE_USER");

        TypeEntityResponse response = new TypeEntityResponse(id, nameType, roles);

        assertEquals(id, response.id());
        assertEquals(nameType, response.nameType());
        assertEquals(roles, response.roles());
    }

    @Test
    void testNullRoles() {
        TypeEntityResponse response = new TypeEntityResponse(2L, "User", null);

        assertEquals(2L, response.id());
        assertEquals("User", response.nameType());
        assertNull(response.roles());
    }
}

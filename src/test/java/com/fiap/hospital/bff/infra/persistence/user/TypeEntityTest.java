package com.fiap.hospital.bff.infra.persistence.user;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeEntityTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        TypeEntity typeEntity = new TypeEntity();
        typeEntity.setId(1L);
        typeEntity.setNameType("Admin");
        typeEntity.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));

        assertEquals(1L, typeEntity.getId());
        assertEquals("Admin", typeEntity.getNameType());
        assertEquals(List.of("ROLE_ADMIN", "ROLE_USER"), typeEntity.getRoles());
    }

    @Test
    void testAllArgsConstructor() {
        TypeEntity typeEntity = new TypeEntity(2L, "User", List.of("ROLE_USER"));

        assertEquals(2L, typeEntity.getId());
        assertEquals("User", typeEntity.getNameType());
        assertEquals(List.of("ROLE_USER"), typeEntity.getRoles());
    }

    @Test
    void testBuilder() {
        TypeEntity typeEntity = TypeEntity.builder()
                .id(3L)
                .nameType("Manager")
                .roles(List.of("ROLE_MANAGER"))
                .build();

        assertEquals(3L, typeEntity.getId());
        assertEquals("Manager", typeEntity.getNameType());
        assertEquals(List.of("ROLE_MANAGER"), typeEntity.getRoles());
    }
}

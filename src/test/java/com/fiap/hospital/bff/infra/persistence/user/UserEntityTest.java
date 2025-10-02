package com.fiap.hospital.bff.infra.persistence.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void testUserEntityBuilderAndGetters() {
        TypeEntity typeEntity = new TypeEntity(1L, "DOCTOR", null);

        UserEntity user = UserEntity.builder()
                .id(10L)
                .name("John Doe")
                .email("john.doe@example.com")
                .login("johndoe")
                .password("password123")
                .changeDate(LocalDate.of(2025, 10, 1))
                .types(typeEntity)
                .build();

        assertEquals(10L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("johndoe", user.getLogin());
        assertEquals("password123", user.getPassword());
        assertEquals(LocalDate.of(2025, 10, 1), user.getChangeDate());
        assertNotNull(user.getTypes());
        assertEquals("DOCTOR", user.getTypes().getNameType());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        UserEntity user = new UserEntity();

        user.setId(20L);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setLogin("janedoe");
        user.setPassword("securePass!");
        user.setChangeDate(LocalDate.now());

        TypeEntity type = new TypeEntity();
        type.setId(2L);
        type.setNameType("NURSE");

        user.setTypes(type);

        assertEquals(20L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("janedoe", user.getLogin());
        assertEquals("securePass!", user.getPassword());
        assertNotNull(user.getChangeDate());
        assertEquals("NURSE", user.getTypes().getNameType());
    }
}

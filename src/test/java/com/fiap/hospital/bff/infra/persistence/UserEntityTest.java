//package com.fiap.hospital.bff.infra.persistence;
//
//import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
//import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserEntityTest {
//
//    @Test
//    void testUserEntityBuilderAndGetters() {
//        TypeEntity typeEntity = new TypeEntity(1L, "DOCTOR", null);
//
//        UserEntity user = UserEntity.builder()
//                .id(10L)
//                .name("John Doe")
//                .email("john.doe@example.com")
//                .login("johndoe")
//                .password("password123")
//                .changeDate(LocalDateTime.of(2025, 10, 1, 10, 0))
//                .type(typeEntity)
//                .build();
//
//        assertEquals(10L, user.getId());
//        assertEquals("John Doe", user.getName());
//        assertEquals("john.doe@example.com", user.getEmail());
//        assertEquals("johndoe", user.getLogin());
//        assertEquals("password123", user.getPassword());
//        assertEquals(LocalDateTime.of(2025, 10, 1, 10, 0), user.getChangeDate());
//        assertNotNull(user.getType());
//        assertEquals("DOCTOR", user.getType().getNameType());
//    }
//
//    @Test
//    void testNoArgsConstructorAndSetters() {
//        UserEntity user = new UserEntity();
//
//        user.setId(20L);
//        user.setName("Jane Smith");
//        user.setEmail("jane.smith@example.com");
//        user.setLogin("janesmith");
//        user.setPassword("securepass");
//        user.setChangeDate(LocalDateTime.of(2025, 10, 2, 14, 30));
//
//        assertEquals(20L, user.getId());
//        assertEquals("Jane Smith", user.getName());
//        assertEquals("jane.smith@example.com", user.getEmail());
//        assertEquals("janesmith", user.getLogin());
//        assertEquals("securepass", user.getPassword());
//        assertEquals(LocalDateTime.of(2025, 10, 2, 14, 30), user.getChangeDate());
//    }
//}

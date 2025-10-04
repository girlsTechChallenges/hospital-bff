//package com.fiap.hospital.bff.core.domain.model.user;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TypeTest {
//
//    @Test
//    void testNoArgsConstructorAndSetters() {
//        Type type = new Type();
//        type.setId(1L);
//        type.setNameType("ADMIN");
//        type.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
//
//        assertEquals(1L, type.getId());
//        assertEquals("ADMIN", type.getNameType());
//        assertEquals(2, type.getRoles().size());
//        assertTrue(type.getRoles().contains("ROLE_ADMIN"));
//    }
//
//    @Test
//    void testAllArgsConstructor() {
//        List<String> roles = Arrays.asList("ROLE_USER");
//        Type type = new Type(2L, "USER", roles);
//
//        assertEquals(2L, type.getId());
//        assertEquals("USER", type.getNameType());
//        assertEquals(roles, type.getRoles());
//    }
//
//    @Test
//    void testEmptyRolesList() {
//        Type type = new Type(3L, "GUEST", Collections.emptyList());
//
//        assertNotNull(type.getRoles());
//        assertTrue(type.getRoles().isEmpty());
//    }
//
//    @Test
//    void testToStringNotNull() {
//        Type type = new Type(4L, "MODERATOR", Arrays.asList("ROLE_MOD"));
//        assertNotNull(type.toString()); // ToString gerado pelo Lombok
//    }
//}

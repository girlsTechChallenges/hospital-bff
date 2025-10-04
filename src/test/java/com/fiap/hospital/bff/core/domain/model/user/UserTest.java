//package com.fiap.hospital.bff.core.domain.model.user;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserTest {
//
//    @Test
//    void testAllArgsConstructor() {
//        Type type = new Type(1L, "ADMIN", Arrays.asList("ROLE_ADMIN"));
//        LocalDate changeDate = LocalDate.of(2023, 10, 1);
//        User user = new User(1L, "John Doe", "john@example.com", "jdoe", "password123", changeDate, type);
//
//        assertEquals(1L, user.getId());
//        assertEquals("John Doe", user.getName());
//        assertEquals("john@example.com", user.getEmail());
//        assertEquals("jdoe", user.getLogin());
//        assertEquals("password123", user.getPassword());
//        assertEquals(changeDate, user.getChangeDate());
//        assertEquals(type, user.getType());
//    }
//
//    @Test
//    void testPartialConstructor() {
//        Type type = new Type(2L, "USER", Arrays.asList("ROLE_USER"));
//        User user = new User("Jane Doe", "jane@example.com", "securePass", type);
//
//        assertNull(user.getId());
//        assertNull(user.getLogin());
//        assertNull(user.getChangeDate());
//
//        assertEquals("Jane Doe", user.getName());
//        assertEquals("jane@example.com", user.getEmail());
//        assertEquals("securePass", user.getPassword());
//        assertEquals(type, user.getType());
//    }
//
//    @Test
//    void testSettersAndGetters() {
//        User user = new User();
//        LocalDate today = LocalDate.now();
//        Type type = new Type(3L, "GUEST", Arrays.asList("ROLE_GUEST"));
//
//        user.setId(5L);
//        user.setName("Alice");
//        user.setEmail("alice@example.com");
//        user.setLogin("alice123");
//        user.setPassword("pass123");
//        user.setChangeDate(today);
//        user.setType(type);
//
//        assertEquals(5L, user.getId());
//        assertEquals("Alice", user.getName());
//        assertEquals("alice@example.com", user.getEmail());
//        assertEquals("alice123", user.getLogin());
//        assertEquals("pass123", user.getPassword());
//        assertEquals(today, user.getChangeDate());
//        assertEquals(type, user.getType());
//    }
//
//    @Test
//    void testToStringNotNull() {
//        User user = new User("Bob", "bob@example.com", "pass", new Type(4L, "NURSE", Arrays.asList("ROLE_NURSE")));
//        assertNotNull(user.toString());  // Verifica se @ToString do Lombok está funcionando
//    }
//}

//package com.fiap.hospital.bff.core.domain.model.token;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TokenTest {
//
//    @Test
//    void testNoArgsConstructorAndSetters() {
//        Token token = new Token();
//        token.setAccessToken("abc123");
//        token.setExpiresIn(3600L);
//
//        assertEquals("abc123", token.getAccessToken());
//        assertEquals(3600L, token.getExpiresIn());
//    }
//
//    @Test
//    void testAllArgsConstructor() {
//        Token token = new Token("xyz789", 7200L, List.of("123"));
//
//        assertEquals("xyz789", token.getAccessToken());
//        assertEquals(7200L, token.getExpiresIn());
//    }
//
//    @Test
//    void testSetAndGetAccessToken() {
//        Token token = new Token();
//        token.setAccessToken("tokenValue");
//
//        assertEquals("tokenValue", token.getAccessToken());
//    }
//
//    @Test
//    void testSetAndGetExpiresIn() {
//        Token token = new Token();
//        token.setExpiresIn(12345L);
//
//        assertEquals(12345L, token.getExpiresIn());
//    }
//}

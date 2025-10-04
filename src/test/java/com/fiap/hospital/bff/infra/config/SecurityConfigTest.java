//package com.fiap.hospital.bff.infra.config;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.security.KeyPairGenerator;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SecurityConfigTest {
//
//    private SecurityConfig securityConfig;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        // Gerar par de chaves RSA para testes
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//
//        securityConfig = new SecurityConfig();
//    }
//
//    @Test
//    void testBCryptPasswordEncoder() {
//        BCryptPasswordEncoder encoder = securityConfig.bCryptPasswordEncoder();
//        assertNotNull(encoder);
//        assertTrue(encoder.matches("password", encoder.encode("password")));
//    }
//}

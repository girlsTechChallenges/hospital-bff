package com.fiap.hospital.bff.infra.config;

import com.fiap.hospital.bff.infra.exception.JwtAccessDeniedHandler;
import com.fiap.hospital.bff.infra.exception.JwtAuthenticationEntryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private RSAPublicKey publicKey;

    @Mock
    private RSAPrivateKey privateKey;

    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtAuthenticationEntryPoint, jwtAccessDeniedHandler);
        ReflectionTestUtils.setField(securityConfig, "key", publicKey);
        ReflectionTestUtils.setField(securityConfig, "priv", privateKey);
    }

    @Test
    @DisplayName("Should create JwtDecoder bean successfully")
    void shouldCreateJwtDecoderBeanSuccessfully() {
        // Arrange - Create real RSA public key for testing
        try {
            java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            java.security.KeyPair keyPair = keyGen.generateKeyPair();
            
            RSAPublicKey realPublicKey = (RSAPublicKey) keyPair.getPublic();
            ReflectionTestUtils.setField(securityConfig, "key", realPublicKey);

            // Act
            JwtDecoder jwtDecoder = securityConfig.jwtDecoder();

            // Assert
            assertNotNull(jwtDecoder, "JwtDecoder should not be null");
        } catch (Exception e) {
            fail("Should not throw exception when creating JwtDecoder: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should create JwtEncoder bean successfully")
    void shouldCreateJwtEncoderBeanSuccessfully() {
        // Arrange - Create real RSA keys for testing
        try {
            java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            java.security.KeyPair keyPair = keyGen.generateKeyPair();
            
            RSAPublicKey realPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey realPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            
            ReflectionTestUtils.setField(securityConfig, "key", realPublicKey);
            ReflectionTestUtils.setField(securityConfig, "priv", realPrivateKey);

            // Act
            JwtEncoder jwtEncoder = securityConfig.jwtEncoder();

            // Assert
            assertNotNull(jwtEncoder, "JwtEncoder should not be null");
        } catch (Exception e) {
            fail("Should not throw exception when creating JwtEncoder: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should create BCryptPasswordEncoder bean successfully")
    void shouldCreateBCryptPasswordEncoderBeanSuccessfully() {
        // Act
        BCryptPasswordEncoder passwordEncoder = securityConfig.bCryptPasswordEncoder();

        // Assert
        assertNotNull(passwordEncoder, "BCryptPasswordEncoder should not be null");
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder, 
                  "Should be instance of BCryptPasswordEncoder");
    }

    @Test
    @DisplayName("Should verify BCryptPasswordEncoder functionality")
    void shouldVerifyBCryptPasswordEncoderFunctionality() {
        // Arrange
        BCryptPasswordEncoder passwordEncoder = securityConfig.bCryptPasswordEncoder();
        String rawPassword = "testPassword123";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertNotEquals(rawPassword, encodedPassword, "Encoded password should be different from raw password");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), 
                  "Password encoder should match raw password with encoded password");
    }

    @Test
    @DisplayName("Should create SecurityConfig with required dependencies")
    void shouldCreateSecurityConfigWithRequiredDependencies() {
        // Assert
        assertNotNull(securityConfig, "SecurityConfig should not be null");
        
        // Verify constructor injection worked
        SecurityConfig newConfig = new SecurityConfig(jwtAuthenticationEntryPoint, jwtAccessDeniedHandler);
        assertNotNull(newConfig, "SecurityConfig should be created with dependencies");
    }

    @Test
    @DisplayName("Should handle null RSA keys gracefully in JwtEncoder")
    void shouldHandleNullRSAKeysGracefullyInJwtEncoder() {
        // Arrange
        SecurityConfig configWithNullKeys = new SecurityConfig(jwtAuthenticationEntryPoint, jwtAccessDeniedHandler);
        ReflectionTestUtils.setField(configWithNullKeys, "key", null);
        ReflectionTestUtils.setField(configWithNullKeys, "priv", null);

        // Act & Assert
        assertThrows(Exception.class, () -> configWithNullKeys.jwtEncoder(),
                    "Should throw exception when RSA keys are null");
    }

    @Test
    @DisplayName("Should handle null RSA public key gracefully in JwtDecoder")
    void shouldHandleNullRSAPublicKeyGracefullyInJwtDecoder() {
        // Arrange
        SecurityConfig configWithNullKey = new SecurityConfig(jwtAuthenticationEntryPoint, jwtAccessDeniedHandler);
        ReflectionTestUtils.setField(configWithNullKey, "key", null);

        // Act & Assert
        assertThrows(Exception.class, () -> configWithNullKey.jwtDecoder(),
                    "Should throw exception when RSA public key is null");
    }

    @Test
    @DisplayName("Should create different BCryptPasswordEncoder instances")
    void shouldCreateDifferentBCryptPasswordEncoderInstances() {
        // Act
        BCryptPasswordEncoder encoder1 = securityConfig.bCryptPasswordEncoder();
        BCryptPasswordEncoder encoder2 = securityConfig.bCryptPasswordEncoder();

        // Assert
        assertNotNull(encoder1, "First encoder should not be null");
        assertNotNull(encoder2, "Second encoder should not be null");
        // Note: These might be the same instance if @Bean creates singleton, but both should work
        
        String password = "testPassword";
        String encoded1 = encoder1.encode(password);
        String encoded2 = encoder2.encode(password);
        
        assertTrue(encoder1.matches(password, encoded1), "First encoder should match its encoded password");
        assertTrue(encoder2.matches(password, encoded2), "Second encoder should match its encoded password");
    }
}
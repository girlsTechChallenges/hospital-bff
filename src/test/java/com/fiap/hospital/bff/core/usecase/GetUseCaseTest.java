package com.fiap.hospital.bff.core.usecase;

import com.fiap.hospital.bff.core.domain.model.token.Token;
import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.infra.exception.UserCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetUseCaseTest {

    private GetGateway getGateway;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private GetUseCase getUseCase;

    @BeforeEach
    void setUp() {
        getGateway = mock(GetGateway.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        jwtEncoder = mock(JwtEncoder.class);
        getUseCase = new GetUseCase(getGateway, passwordEncoder, jwtEncoder);
    }

    @Test
    void testValidateLoginSuccess() {
        // Arrange
        String email = "user@example.com";
        String rawPassword = "plain123";
        String encodedPassword = "$2a$10$ABCDEF123456789"; // simulated encoded password
        String jwtToken = "jwt.token.string";

        Type type = new Type(1L, "ADMIN", List.of("ROLE_ADMIN"));
        User user = new User(1L, "User", email, "login", encodedPassword, null, type);

        when(getGateway.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(jwtToken);
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        // Act
        Token result = getUseCase.validateLogin(email, rawPassword);

        // Assert
        assertNotNull(result);
        assertEquals(jwtToken, result.getAccessToken());
        assertEquals(300L, result.getExpiresIn());

        verify(getGateway, times(2)).findByEmail(email); // called in both methods
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
        verify(jwtEncoder).encode(any());
    }

    @Test
    void testValidateLoginInvalidPassword() {
        String email = "user@example.com";
        String wrongPassword = "wrongpass";
        String correctEncodedPassword = "$2a$10$encoded";

        User user = new User(1L, "User", email, "login", correctEncodedPassword, null,
                new Type(1L, "ADMIN", List.of("ROLE_ADMIN")));

        when(getGateway.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, correctEncodedPassword)).thenReturn(false);

        assertThrows(UserCredentialsException.class, () ->
            getUseCase.validateLogin(email, wrongPassword)
        );

        verify(getGateway, times(2)).findByEmail(email);
        verify(passwordEncoder).matches(wrongPassword, correctEncodedPassword);
        verify(jwtEncoder, never()).encode(any());
    }
}

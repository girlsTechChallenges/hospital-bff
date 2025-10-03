package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.inputport.ConsultCommandUseCase;
import com.fiap.hospital.bff.core.inputport.ConsultQueryUseCase;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserAuthDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserCredentialsDto;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import com.fiap.hospital.bff.core.domain.model.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private ConsultCommandUseCase consultCommandUseCase;

    @Mock
    private ConsultQueryUseCase consultQueryUseCase;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnTokenResponse() {
        // Arrange
        String email = "user@example.com";
        String password = "password123";

        UserCredentialsDto loginRequest = new UserCredentialsDto(email, password);
        Token token = new Token("jwt-token", 300L, List.of("123"));
        UserAuthDto tokenResponseDto = new UserAuthDto("jwt-token", 300L, List.of("123"));

        when(consultQueryUseCase.validateLogin(email, password)).thenReturn(token);
        when(userMapper.toTokenResponseDto(token)).thenReturn(tokenResponseDto);

        // Act
        ResponseEntity<UserAuthDto> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().accessToken());
        assertEquals(300L, response.getBody().expiresIn());

        verify(consultQueryUseCase).validateLogin(email, password);
        verify(userMapper).toTokenResponseDto(token);
    }

    @Test
    void updatePassword_shouldCallUseCaseAndReturnNoContent() {
        // Arrange
        String email = "user@example.com";
        String password = "newPassword";

        UserCredentialsDto request = new UserCredentialsDto(email, password);

        // Act
        ResponseEntity<Void> response = authController.updatePassword(request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(consultCommandUseCase).updatePassword(email, password);
    }
}

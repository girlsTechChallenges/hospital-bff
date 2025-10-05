package com.fiap.hospital.bff.infra.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAccessDeniedHandler Tests")
class JwtAccessDeniedHandlerTest {

    @InjectMocks
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AccessDeniedException accessDeniedException;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("Should handle access denied exception and return forbidden response")
    void shouldHandleAccessDeniedExceptionAndReturnForbiddenResponse() throws IOException {
        // Arrange
        String requestURI = "/api/v1/protected-resource";
        when(request.getRequestURI()).thenReturn(requestURI);

        // Act
        jwtAccessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(request).getRequestURI();

        printWriter.flush();
        String responseContent = stringWriter.toString();

        // Verify JSON response contains expected fields
        assertTrue(responseContent.contains("\"status\":403"));
        assertTrue(responseContent.contains("\"error\":\"Forbidden\""));
        assertTrue(responseContent.contains("\"message\":\"Access denied. You do not have permission to access this resource..\""));
        assertTrue(responseContent.contains("\"path\":\"" + requestURI + "\""));
        assertTrue(responseContent.contains("\"timestamp\""));
    }

    @Test
    @DisplayName("Should handle access denied exception with null request URI")
    void shouldHandleAccessDeniedExceptionWithNullRequestURI() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn(null);

        // Act
        jwtAccessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        printWriter.flush();
        String responseContent = stringWriter.toString();

        assertTrue(responseContent.contains("\"path\":null"));
    }

    @Test
    @DisplayName("Should handle access denied exception with empty request URI")
    void shouldHandleAccessDeniedExceptionWithEmptyRequestURI() throws IOException {
        // Arrange
        String requestURI = "";
        when(request.getRequestURI()).thenReturn(requestURI);

        // Act
        jwtAccessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        printWriter.flush();
        String responseContent = stringWriter.toString();

        assertTrue(responseContent.contains("\"path\":\"\""));
    }

    @Test
    @DisplayName("Should handle IOException during response writing")
    void shouldHandleIOExceptionDuringResponseWriting() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        when(response.getWriter()).thenThrow(new IOException("Writer error"));

        // Act & Assert
        assertThrows(IOException.class, () -> {
            jwtAccessDeniedHandler.handle(request, response, accessDeniedException);
        });
    }

    @Test
    @DisplayName("Should create valid JSON response format")
    void shouldCreateValidJsonResponseFormat() throws IOException {
        // Arrange
        String requestURI = "/api/v1/users";
        when(request.getRequestURI()).thenReturn(requestURI);

        // Act
        jwtAccessDeniedHandler.handle(request, response, accessDeniedException);

        // Assert
        printWriter.flush();
        String responseContent = stringWriter.toString();

        // Verify it's valid JSON by parsing it
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> objectMapper.readTree(responseContent));

        // Verify specific JSON structure
        assertTrue(responseContent.startsWith("{"));
        assertTrue(responseContent.endsWith("}"));
        assertTrue(responseContent.contains("\"timestamp\""));
        assertTrue(responseContent.contains("\"status\""));
        assertTrue(responseContent.contains("\"error\""));
        assertTrue(responseContent.contains("\"message\""));
        assertTrue(responseContent.contains("\"path\""));
    }
}
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
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationEntryPoint Tests")
class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authenticationException;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("Should commence authentication and return unauthorized response")
    void shouldCommenceAuthenticationAndReturnUnauthorizedResponse() throws IOException {
        // Arrange
        String requestURI = "/api/v1/protected-resource";
        when(request.getRequestURI()).thenReturn(requestURI);

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(request).getRequestURI();

        printWriter.flush();
        String responseContent = stringWriter.toString();

        // Verify JSON response contains expected fields
        assertTrue(responseContent.contains("\"status\":401"));
        assertTrue(responseContent.contains("\"error\":\"Unauthorized\""));
        assertTrue(responseContent.contains("\"message\":\"Invalid or missing JWT token. Log in to access this resource.\""));
        assertTrue(responseContent.contains("\"path\":\"" + requestURI + "\""));
        assertTrue(responseContent.contains("\"timestamp\""));
    }

    @Test
    @DisplayName("Should handle authentication exception with null request URI")
    void shouldHandleAuthenticationExceptionWithNullRequestURI() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn(null);

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        printWriter.flush();
        String responseContent = stringWriter.toString();

        assertTrue(responseContent.contains("\"path\":null"));
    }

    @Test
    @DisplayName("Should handle authentication exception with empty request URI")
    void shouldHandleAuthenticationExceptionWithEmptyRequestURI() throws IOException {
        // Arrange
        String requestURI = "";
        when(request.getRequestURI()).thenReturn(requestURI);

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
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
            jwtAuthenticationEntryPoint.commence(request, response, authenticationException);
        });
    }

    @Test
    @DisplayName("Should create valid JSON response format")
    void shouldCreateValidJsonResponseFormat() throws IOException {
        // Arrange
        String requestURI = "/api/v1/users";
        when(request.getRequestURI()).thenReturn(requestURI);

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

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

    @Test
    @DisplayName("Should handle different request URIs correctly")
    void shouldHandleDifferentRequestURIsCorrectly() throws IOException {
        // Test data
        String[] testURIs = {
            "/api/v1/users",
            "/api/v1/consults",
            "/api/v1/auth/protected",
            "/api/v2/some-resource",
            "/",
            "/health"
        };

        for (String uri : testURIs) {
            // Reset mocks
            reset(response);
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(printWriter);
            when(request.getRequestURI()).thenReturn(uri);

            // Act
            jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

            // Assert
            printWriter.flush();
            String responseContent = stringWriter.toString();
            assertTrue(responseContent.contains("\"path\":\"" + uri + "\""),
                    "Should contain correct path for URI: " + uri);
        }
    }

    @Test
    @DisplayName("Should set correct HTTP status and content type")
    void shouldSetCorrectHttpStatusAndContentType() throws IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authenticationException);

        // Assert
        verify(response, times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response, times(1)).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response, times(1)).getWriter();
        verifyNoMoreInteractions(response);
    }
}
package com.fiap.hospital.bff.infra.entrypoint.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiap.hospital.bff.core.inputport.AuthenticationCommandUseCase;
import com.fiap.hospital.bff.core.inputport.AuthenticationQueryUseCase;
import com.fiap.hospital.bff.infra.entrypoint.controller.docs.AuthControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserAuthRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserCredentialsRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationCommandUseCase authenticationCommandUseCase;
    private final AuthenticationQueryUseCase authenticationQueryUseCase;
    private final UserMapper userMapper;

    public AuthController(AuthenticationCommandUseCase authenticationCommandUseCase,
                         AuthenticationQueryUseCase authenticationQueryUseCase,
                         UserMapper userMapper) {
        this.authenticationCommandUseCase = authenticationCommandUseCase;
        this.authenticationQueryUseCase = authenticationQueryUseCase;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuthRequestDto> login(@Valid @RequestBody UserCredentialsRequestDto loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.email());
        var response = authenticationQueryUseCase.validateLogin(loginRequest.email(), loginRequest.password());
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toTokenResponseDto(response));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UserCredentialsRequestDto request) {
        log.info("Password update request for email: {}", request.email());
        authenticationCommandUseCase.updatePassword(request.email(), request.password());
        return ResponseEntity.noContent().build();
    }
}

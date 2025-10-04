package com.fiap.hospital.bff.infra.entrypoint.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiap.hospital.bff.core.inputport.ConsultCommandUseCase;
import com.fiap.hospital.bff.core.inputport.ConsultQueryUseCase;
import com.fiap.hospital.bff.infra.entrypoint.docs.AuthControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserAuthRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserCredentialsRequestDto;
import com.fiap.hospital.bff.infra.mapper.UserMapper;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final ConsultCommandUseCase consultCommandUseCase;
    private final ConsultQueryUseCase consultQueryUseCase;
    private final UserMapper userMapper;

    public AuthController(ConsultCommandUseCase consultCommandUseCase, ConsultQueryUseCase consultQueryUseCase, UserMapper userMapper) {
        this.consultCommandUseCase = consultCommandUseCase;
        this.consultQueryUseCase = consultQueryUseCase;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuthRequestDto> login(@Valid @RequestBody UserCredentialsRequestDto loginRequest) {
        var response = consultQueryUseCase.validateLogin(loginRequest.email(), loginRequest.password());
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toAuthDto(response));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody UserCredentialsRequestDto request) {
        log.info("UPDATE USER PASSWORD {}", request);
        consultCommandUseCase.updatePassword(request.email(), request.password());
        return ResponseEntity.noContent().build();
    }
}

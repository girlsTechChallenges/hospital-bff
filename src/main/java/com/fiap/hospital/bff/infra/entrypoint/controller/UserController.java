package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserUpdateRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fiap.hospital.bff.core.inputport.UserCommandUseCase;
import com.fiap.hospital.bff.core.inputport.UserQueryUseCase;
import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.infra.entrypoint.controller.docs.UserControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.entrypoint.mapper.UserMapper;
import com.fiap.hospital.bff.infra.exception.UserNotFoundException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserCommandUseCase userCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;
    private final UserMapper userMapper;

    public UserController(UserCommandUseCase userCommandUseCase,
                          UserQueryUseCase userQueryUseCase,
                          UserMapper userMapper) {
        this.userCommandUseCase = userCommandUseCase;
        this.userQueryUseCase = userQueryUseCase;
        this.userMapper = userMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Creating new user with email: {}", userRequestDto.email());

        User userDomain = userMapper.toUserDomain(userRequestDto);
        User createdUser = userCommandUseCase.createUser(userDomain);
        UserResponseDto response = userMapper.toUserResponseDto(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Fetching all users");

        List<User> users = userQueryUseCase.getAllUsers();
        List<UserResponseDto> response = users.stream()
                .map(userMapper::toUserResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Fetching user by ID: {}", id);

        Optional<User> user = userQueryUseCase.getUserById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        UserResponseDto response = userMapper.toUserResponseDto(user.get());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserUpdateRequestDto updateRequestDto) {
        log.info("Updating user with ID: {}", id);

        User userDomain = userMapper.toUserDomainUpdate(updateRequestDto);
        Optional<User> updatedUser = userCommandUseCase.updateUser(id, userDomain);

        if (updatedUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        UserResponseDto response = userMapper.toUserResponseDto(updatedUser.get());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);

        Optional<User> deletedUser = userCommandUseCase.deleteUser(id);

        if (deletedUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return ResponseEntity.noContent().build();
    }
}
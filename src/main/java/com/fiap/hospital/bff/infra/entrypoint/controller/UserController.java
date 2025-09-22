package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.domain.model.user.User;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.core.usecase.CreateUseCase;
import com.fiap.hospital.bff.core.usecase.DeleteUseCase;
import com.fiap.hospital.bff.core.usecase.GetUseCase;
import com.fiap.hospital.bff.core.usecase.UpdateUseCase;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
import com.fiap.hospital.bff.infra.mapper.UserMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final SaveGateway saveGateway;
    private final GetGateway getGateway;
    private final UpdateGateway updateGateway;
    private final DeleteGateway deleteGateway;
    private final UserMapper userEntityMapper;

    public UserController(SaveGateway saveGateway, GetGateway getAllUseCases, UpdateGateway updateGateway, DeleteGateway deleteGateway, UserMapper userEntityMapper) {
        this.saveGateway = saveGateway;
        this.getGateway = getAllUseCases;
        this.updateGateway = updateGateway;
        this.deleteGateway = deleteGateway;
        this.userEntityMapper = userEntityMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserDto userRequestDto) {
        log.info("POST USER REQUEST: {} ", userRequestDto);
        var resp = saveGateway.save(userEntityMapper.toUserDomain(userRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityMapper.toUserResponseDto(resp));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDto>> getAll() {
        log.info("START GET ALL USERS");
        var resp = getGateway.getAll();
        return ResponseEntity.ok(resp.stream().map(userEntityMapper::toUserResponseDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable @NotNull Long id) {
        log.info("GET USER BY ID REQUEST {} ", id);
        Optional<User> user = getGateway.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userEntityMapper.getUserByIdToUserResponseDto(user));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> update(@PathVariable @NotNull Long id, @RequestBody @Valid UpdateRequestDto updateRequestDto) {
        log.info("UPDATE USER REQUEST {} ", updateRequestDto);
        var updatedUser = updateGateway.update(id, userEntityMapper.updateUserDomain(updateRequestDto));
        return ResponseEntity.ok(userEntityMapper.getUserByIdToUserResponseDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        log.info("DELETE USER BY ID REQUEST {}", id);
        deleteGateway.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

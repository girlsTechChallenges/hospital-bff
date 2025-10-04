package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.infra.entrypoint.docs.TypeUserControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.TypeResponseDto;
import com.fiap.hospital.bff.infra.mapper.TypeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type-users")
public class TypeController implements TypeUserControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(TypeController.class);
    private final SaveGateway saveGateway;
    private final GetGateway getGateway;
    private final UpdateGateway updateGateway;
    private final DeleteGateway deleteGateway;
    private final TypeMapper typeMapper;

    public TypeController(SaveGateway saveGateway, GetGateway getGateway, UpdateGateway updateGateway, DeleteGateway deleteGateway, TypeMapper typeMapper) {
        this.saveGateway = saveGateway;
        this.getGateway = getGateway;
        this.updateGateway = updateGateway;
        this.deleteGateway = deleteGateway;
        this.typeMapper = typeMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TypeResponseDto> create(@Valid @RequestBody TypeRequestDto request) {
        log.info("Received request to create type type: {}", request);
        var resp = saveGateway.saveType(typeMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(typeMapper.toResponseDto(resp));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TypeResponseDto>> getAll() {
        log.info("START GET ALL TYPES");
        var resp = getGateway.getAllTypes();
        return ResponseEntity.ok(resp.stream().map(typeMapper::toResponseDto).toList());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TypeResponseDto> update(@PathVariable @NotNull Long id, @Valid @RequestBody TypeRequestDto request) {
        log.info("Received request to update type user: {}", request);

        Type rep = typeMapper.toDomain(request);
        var response = updateGateway.update(id, rep);
        var responseDto = typeMapper.toResponseDto(response);

        log.info("Type user with Name Type: {} updated successfully", rep.getNameType());
        return ResponseEntity.accepted().body(responseDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        log.info("Received request to delete type user with ID: {}", id);

        deleteGateway.deleteTypeById(id);
        log.info("Type user with ID: {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeResponseDto> getById(@PathVariable @NotNull Long id) {
        log.info("Received request to get type user by ID: {}", id);

        var typeUser = getGateway.getTypeById(id);
        if (typeUser.isPresent()) {
            log.info("Type user found: {}", typeUser.get());
            return ResponseEntity.ok(typeMapper.toResponseDto(typeUser.get()));
        }
        log.warn("Type user with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }
}

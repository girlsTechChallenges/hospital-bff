package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.core.domain.model.user.Type;
import com.fiap.hospital.bff.core.outputport.DeleteGateway;
import com.fiap.hospital.bff.core.outputport.GetGateway;
import com.fiap.hospital.bff.core.outputport.SaveGateway;
import com.fiap.hospital.bff.core.outputport.UpdateGateway;
import com.fiap.hospital.bff.infra.entrypoint.controller.docs.TypeUserControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.TypeEntityRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.TypeEntityResponse;
import com.fiap.hospital.bff.infra.mapper.TypeEntityMapper;
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
@RequestMapping("/type-users")
public class TypeController implements TypeUserControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(TypeController.class);
    private final SaveGateway saveGateway;
    private final GetGateway getGateway;
    private final UpdateGateway updateGateway;
    private final DeleteGateway deleteGateway;
    private final TypeEntityMapper typeEntityMapper;

    public TypeController(SaveGateway saveGateway, GetGateway getGateway, UpdateGateway updateGateway, DeleteGateway deleteGateway, TypeEntityMapper typeEntityMapper) {
        this.saveGateway = saveGateway;
        this.getGateway = getGateway;
        this.updateGateway = updateGateway;
        this.deleteGateway = deleteGateway;
        this.typeEntityMapper = typeEntityMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TypeEntityResponse> create(@Valid @RequestBody TypeEntityRequestDto request) {
        log.info("Received request to create type type: {}", request);
        var resp = saveGateway.saveType(typeEntityMapper.toTypeEntityDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(typeEntityMapper.typeEntityResponse(resp));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TypeEntityResponse>> getAll() {
        log.info("START GET ALL TYPES");
        var resp = getGateway.getAllTypes();
        return ResponseEntity.ok(resp.stream().map(typeEntityMapper::toTypeResponseDto).toList());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TypeEntityResponse> update(@PathVariable @NotNull Long id, @Valid @RequestBody TypeEntityRequestDto request) {
        log.info("Received request to update type user: {}", request);

        Type rep = typeEntityMapper.toTypeEntityDomain(request);
        var response = updateGateway.update(id, rep);
        var responseDto = typeEntityMapper.typeEntityResponse(response);

        log.info("Type user with ID: {} updated successfully", id);
        return ResponseEntity.accepted().body(responseDto);
    }

    @Override
    public ResponseEntity<TypeEntityResponse> getById(Long id) {
        log.info("GET TYPE BY ID REQUEST {} ", id);
        Optional<Type> type = getGateway.getTypeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(typeEntityMapper.getTypeByIdToTypeResponseDto(type));
    }
}

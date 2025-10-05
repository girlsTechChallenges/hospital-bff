package com.fiap.hospital.bff.infra.entrypoint.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fiap.hospital.bff.infra.entrypoint.controller.docs.EasyConsultControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultResponseDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultAggregatedDto;
import com.fiap.hospital.bff.infra.adapter.easyconsult.EasyConsultService;

@RestController
@RequestMapping("/api/v1/consults")
public class EasyConsultController implements EasyConsultControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(EasyConsultController.class);

    private final EasyConsultService easyConsultService;

    public EasyConsultController(EasyConsultService easyConsultService) {
        this.easyConsultService = easyConsultService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultResponseDto> create(@Valid @RequestBody ConsultRequestDto consultRequest) {
        log.info("Creating new consult: {}", consultRequest);
        var response = easyConsultService.createConsult(consultRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
     
    @GetMapping("/{id}")
    public ResponseEntity<ConsultResponseDto> getById(@PathVariable @NotNull Long id) {
        log.info("Fetching consult by ID: {}", id);
        var response = easyConsultService.getConsultById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ConsultAggregatedDto> getByIdWithDetails(@PathVariable @NotNull Long id) {
        log.info("Fetching consult with aggregated details for ID: {}", id);
        var response = easyConsultService.getConsultWithDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConsultResponseDto>> getByPatientId(@PathVariable @NotNull Long patientId) {
        log.info("Fetching consults for patient ID: {}", patientId);
        var response = easyConsultService.getConsultsByPatient(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConsultResponseDto>> getAll() {
        log.info("Fetching all consults");
        var response = easyConsultService.getAllConsults();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultResponseDto> update(@PathVariable @NotNull Long id,
                                                     @Valid @RequestBody ConsultUpdateRequestDto consultUpdateRequest) {
        log.info("Updating consult ID {} with data: {}", id, consultUpdateRequest);
        easyConsultService.updateConsult(id, consultUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}

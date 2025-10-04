package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.infra.entrypoint.docs.EasyConsultControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consults")
public class EasyConsultController implements EasyConsultControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(EasyConsultController.class);
    private static final String CONSULT_SERVICE_URL = "http://kong:8000/consults/";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultResponseDto> create(@Valid @RequestBody ConsultRequestDto consultaRequest) {
        log.info("POST CONSULT REQUEST: {} ", consultaRequest);
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = "http://kong:8000/consults";
        ConsultResponseDto response = restTemplate.postForObject(externalUrl, consultaRequest, ConsultResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
     
    @GetMapping("/{id}")
    public ResponseEntity<ConsultResponseDto> getById(@PathVariable @NotNull Long id) {
        log.info("GET CONSULT BY ID REQUEST {} ", id);
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = CONSULT_SERVICE_URL + id;
        var response = restTemplate.getForObject(externalUrl, ConsultResponseDto.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConsultResponseDto>> getByPatientId(@PathVariable @NotNull Long patientId) {
        log.info("GET CONSULT BY PATIENT ID REQUEST {} ", patientId);
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = CONSULT_SERVICE_URL + "patient/" + patientId;
        var response = restTemplate.getForObject(externalUrl, ConsultResponseDto[].class);
        return ResponseEntity.ok(List.of(response));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConsultResponseDto>> getAll() {
        log.info("GET ALL CONSULTS REQUEST");
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForObject(CONSULT_SERVICE_URL, ConsultResponseDto[].class);
        return ResponseEntity.ok(List.of(response));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@NotNull Long id,
            @Valid ConsultUpdateRequestDto consultUpdateRequest) {
        log.info("PUT CONSULT REQUEST {} ", consultUpdateRequest);
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = CONSULT_SERVICE_URL + id;
        restTemplate.put(externalUrl, consultUpdateRequest);
        return ResponseEntity.ok().build();
    }
    
}

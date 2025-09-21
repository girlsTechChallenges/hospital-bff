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
import org.springframework.web.client.RestTemplate;
import com.fiap.hospital.bff.infra.entrypoint.controller.docs.EasyConsultControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.ConsultDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.ConsultUpdateDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.ConsultResponseDto;

@RestController
@RequestMapping("/consults")
public class EasyConsultController implements EasyConsultControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(EasyConsultController.class);             

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultResponseDto> create(@Valid @RequestBody ConsultDto consultaRequest) {
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
        String externalUrl = "http://kong:8000/consults/" + id;
        var response = restTemplate.getForObject(externalUrl, ConsultResponseDto.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConsultResponseDto>> getByPatientId(@PathVariable @NotNull Long patientId) {
        log.info("GET CONSULT BY PATIENT ID REQUEST {} ", patientId);
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = "http://kong:8000/consults/patient/" + patientId;
        var response = restTemplate.getForObject(externalUrl, ConsultResponseDto[].class);
        return ResponseEntity.ok(List.of(response));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConsultResponseDto>> getAll() {
        log.info("GET ALL CONSULTS REQUEST");
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = "http://kong:8000/consults/";
        var response = restTemplate.getForObject(externalUrl, ConsultResponseDto[].class);
        return ResponseEntity.ok(List.of(response));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@NotNull Long id,
            @Valid ConsultUpdateDto consultUpdateRequest) {
        log.info("PUT CONSULT REQUEST {} ", consultUpdateRequest);
        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = "http://kong:8000/consults/" + id;
        restTemplate.put(externalUrl, consultUpdateRequest);
        return ResponseEntity.ok().build();
    }
    
}

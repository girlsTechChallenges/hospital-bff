package com.fiap.hospital.bff.infra.entrypoint.controller;

import com.fiap.hospital.bff.infra.adapter.easyconsult.EasyConsultService;
import com.fiap.hospital.bff.infra.entrypoint.controller.docs.EasyConsultControllerDocs;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultFilterDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.GraphQLConsultResponse;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultDeleteRequestDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consults")
public class EasyConsultController implements EasyConsultControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(EasyConsultController.class);

    private final EasyConsultService easyConsultService;

    public EasyConsultController(EasyConsultService easyConsultService) {
        this.easyConsultService = easyConsultService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphQLConsultResponse> create(@Valid @RequestBody ConsultRequestDto consultRequest) {
        log.info("Creating new consult: {}", consultRequest);
        var response = easyConsultService.createConsult(consultRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GraphQLConsultResponse>> getAll() {
        log.info("Fetching all consults");
        var response = easyConsultService.getAllConsults();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GraphQLConsultResponse>> getByFilter(
            @RequestParam(required = false) String patientEmail,
            @RequestParam(required = false) String professionalEmail,
            @RequestParam(required = false) String localTime,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status) {

        log.info("Fetching consults with filters");

        ConsultFilterDto filter = new ConsultFilterDto(patientEmail, professionalEmail, localTime, date, status);
        var response = easyConsultService.getConsultsByFilter(filter);
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphQLConsultResponse> update(@Valid @RequestBody ConsultUpdateRequestDto updateRequest) {
        log.info("Updating consult: {}", updateRequest);
        var response = easyConsultService.updateConsult(updateRequest);
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@Valid @RequestBody ConsultDeleteRequestDto deleteRequest) {
        log.info("Deleting consult: {}", deleteRequest);
        var response = easyConsultService.deleteConsult(deleteRequest);
        return ResponseEntity.ok(response);
    }

}

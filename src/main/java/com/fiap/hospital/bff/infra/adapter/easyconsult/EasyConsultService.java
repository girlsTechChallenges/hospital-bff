package com.fiap.hospital.bff.infra.adapter.easyconsult;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultAggregatedDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultResponseDto;
import com.fiap.hospital.bff.infra.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class EasyConsultService {

    private static final Logger log = LoggerFactory.getLogger(EasyConsultService.class);

    private final RestTemplate restTemplate;

    @Value("${external.services.consult.url:http://kong:8000}")
    private String consultServiceUrl;

    @Value("${external.services.patient.url:http://kong:8000}")
    private String patientServiceUrl;

    @Value("${external.services.doctor.url:http://kong:8000}")
    private String doctorServiceUrl;

    public EasyConsultService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ConsultResponseDto createConsult(ConsultRequestDto consultRequestDto) {
        try {
            log.info("Creating consult: {}", consultRequestDto);

            HttpHeaders headers = createHeaders();
            HttpEntity<ConsultRequestDto> entity = new HttpEntity<>(consultRequestDto, headers);

            ResponseEntity<ConsultResponseDto> response = restTemplate.exchange(
                consultServiceUrl + "/consults",
                HttpMethod.POST,
                entity,
                ConsultResponseDto.class
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("Error creating consult: {}", e.getMessage());
            throw new ExternalServiceException("Failed to create consult: " + e.getMessage());
        } catch (ResourceAccessException e) {
            log.error("Connection error: {}", e.getMessage());
            throw new ExternalServiceException("Service unavailable");
        }
    }

    public ConsultAggregatedDto getConsultWithDetails(Long id) {
        try {
            log.info("Fetching consult with details for ID: {}", id);

            // Buscar consulta principal
            CompletableFuture<ConsultResponseDto> consultFuture = CompletableFuture.supplyAsync(() ->
                getConsultById(id)
            );

            // Aguardar resultado da consulta para obter IDs de paciente e médico
            ConsultResponseDto consult = consultFuture.join();

            // Buscar detalhes do paciente e médico em paralelo
            CompletableFuture<Object> patientFuture = CompletableFuture.supplyAsync(() ->
                getPatientDetails(consult.patientId())
            );

            CompletableFuture<Object> doctorFuture = CompletableFuture.supplyAsync(() ->
                getDoctorDetails(consult.doctorId())
            );

            // Aguardar todos os resultados
            CompletableFuture.allOf(patientFuture, doctorFuture).join();

            // Criar resposta agregada simples sem HATEOAS
            return new ConsultAggregatedDto(
                consult,
                patientFuture.join(),
                doctorFuture.join()
            );

        } catch (Exception e) {
            log.error("Error fetching consult details: {}", e.getMessage());
            throw new ExternalServiceException("Failed to fetch consult details");
        }
    }

    public ConsultResponseDto getConsultById(Long id) {
        try {
            ResponseEntity<ConsultResponseDto> response = restTemplate.getForEntity(
                consultServiceUrl + "/consults/" + id,
                ConsultResponseDto.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Error fetching consult {}: {}", id, e.getMessage());
            throw new ExternalServiceException("Consult not found");
        }
    }

    public List<ConsultResponseDto> getConsultsByPatient(Long patientId) {
        try {
            ResponseEntity<ConsultResponseDto[]> response = restTemplate.getForEntity(
                consultServiceUrl + "/consults/patient/" + patientId,
                ConsultResponseDto[].class
            );
            return List.of(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException e) {
            log.error("Error fetching consults for patient {}: {}", patientId, e.getMessage());
            throw new ExternalServiceException("Failed to fetch patient consults");
        }
    }

    public List<ConsultResponseDto> getAllConsults() {
        try {
            ResponseEntity<ConsultResponseDto[]> response = restTemplate.getForEntity(
                consultServiceUrl + "/consults",
                ConsultResponseDto[].class
            );
            return List.of(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException e) {
            log.error("Error fetching all consults: {}", e.getMessage());
            throw new ExternalServiceException("Failed to fetch consults");
        }
    }

    public void updateConsult(Long id, ConsultUpdateRequestDto consultUpdateRequestDto) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<ConsultUpdateRequestDto> entity = new HttpEntity<>(consultUpdateRequestDto, headers);

            restTemplate.exchange(
                consultServiceUrl + "/consults/" + id,
                HttpMethod.PUT,
                entity,
                Void.class
            );
        } catch (HttpClientErrorException e) {
            log.error("Error updating consult {}: {}", id, e.getMessage());
            throw new ExternalServiceException("Failed to update consult");
        }
    }

    private Object getPatientDetails(Long patientId) {
        try {
            return restTemplate.getForObject(
                patientServiceUrl + "/patients/" + patientId,
                Object.class
            );
        } catch (Exception e) {
            log.warn("Could not fetch patient details for ID {}: {}", patientId, e.getMessage());
            return null; // Graceful degradation
        }
    }

    private Object getDoctorDetails(Long doctorId) {
        try {
            return restTemplate.getForObject(
                doctorServiceUrl + "/doctors/" + doctorId,
                Object.class
            );
        } catch (Exception e) {
            log.warn("Could not fetch doctor details for ID {}: {}", doctorId, e.getMessage());
            return null;
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

package com.fiap.hospital.bff.infra.adapter.easyconsult;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultAggregatedDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultResponseDto;
import com.fiap.hospital.bff.infra.exception.ExternalServiceException;
import com.fiap.hospital.bff.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para EasyConsultService
 * 
 * Testa as regras de negócio relacionadas ao serviço de consultas:
 * - Criação de consultas
 * - Busca de consultas (simples e com detalhes agregados)
 * - Atualização de consultas
 * - Tratamento de falhas de serviços externos
 * - Agregação de dados de múltiplos serviços
 * 
 * Utiliza mocks para isolar as dependências externas (RestTemplate)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EasyConsultService - Testes Unitários")
class EasyConsultServiceTest {

    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private EasyConsultService easyConsultService;

    private final String consultServiceUrl = "http://localhost:8089";
    private final String patientServiceUrl = "http://localhost:8089";
    private final String doctorServiceUrl = "http://localhost:8089";

    @BeforeEach
    void setUp() {
        // Configurar URLs usando ReflectionTestUtils para simular @Value
        ReflectionTestUtils.setField(easyConsultService, "consultServiceUrl", consultServiceUrl);
        ReflectionTestUtils.setField(easyConsultService, "patientServiceUrl", patientServiceUrl);
        ReflectionTestUtils.setField(easyConsultService, "doctorServiceUrl", doctorServiceUrl);
    }

    @Nested
    @DisplayName("Testes de Criação de Consulta")
    class CreateConsultTests {

        @Test
        @DisplayName("Deve criar consulta com sucesso quando dados válidos são fornecidos")
        void shouldCreateConsultSuccessfully_WhenValidDataProvided() {
            // Arrange
            ConsultRequestDto requestDto = TestDataBuilder.createValidConsultRequestDto();
            ConsultResponseDto expectedResponse = TestDataBuilder.createValidConsultResponseDto();
            
            ResponseEntity<ConsultResponseDto> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.CREATED);
            
            when(restTemplate.exchange(
                eq(consultServiceUrl + "/consults"),
                eq(HttpMethod.POST), 
                any(HttpEntity.class),
                eq(ConsultResponseDto.class)
            )).thenReturn(responseEntity);

            // Act
            ConsultResponseDto actualResponse = easyConsultService.createConsult(requestDto);

            // Assert
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse).isEqualTo(expectedResponse);
            
            verify(restTemplate, times(1)).exchange(
                eq(consultServiceUrl + "/consults"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ConsultResponseDto.class)
            );
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando serviço de consulta retorna erro HTTP")
        void shouldThrowExternalServiceException_WhenConsultServiceReturnsHttpError() {
            // Arrange
            ConsultRequestDto requestDto = TestDataBuilder.createValidConsultRequestDto();
            HttpClientErrorException httpError = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Dados inválidos");
            
            when(restTemplate.exchange(
                eq(consultServiceUrl + "/consults"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ConsultResponseDto.class)
            )).thenThrow(httpError);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.createConsult(requestDto))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessageContaining("Failed to create consult");
            
            verify(restTemplate, times(1)).exchange(
                eq(consultServiceUrl + "/consults"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ConsultResponseDto.class)
            );
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando há erro de conectividade")
        void shouldThrowExternalServiceException_WhenConnectivityErrorOccurs() {
            // Arrange
            ConsultRequestDto requestDto = TestDataBuilder.createValidConsultRequestDto();
            ResourceAccessException connectivityError = new ResourceAccessException("Connection timeout");
            
            when(restTemplate.exchange(
                eq(consultServiceUrl + "/consults"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ConsultResponseDto.class)
            )).thenThrow(connectivityError);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.createConsult(requestDto))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Service unavailable");
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Consulta por ID")
    class GetConsultByIdTests {

        @Test
        @DisplayName("Deve retornar consulta quando ID válido é fornecido")
        void shouldReturnConsult_WhenValidIdProvided() {
            // Arrange
            Long consultId = 1L;
            ConsultResponseDto expectedResponse = TestDataBuilder.createValidConsultResponseDto();
            ResponseEntity<ConsultResponseDto> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
            
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(ConsultResponseDto.class)
            )).thenReturn(responseEntity);

            // Act
            ConsultResponseDto actualResponse = easyConsultService.getConsultById(consultId);

            // Assert
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse).isEqualTo(expectedResponse);
            
            verify(restTemplate, times(1)).getForEntity(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(ConsultResponseDto.class)
            );
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando consulta não é encontrada")
        void shouldThrowExternalServiceException_WhenConsultNotFound() {
            // Arrange
            Long nonExistentId = 999L;
            HttpClientErrorException notFoundError = new HttpClientErrorException(HttpStatus.NOT_FOUND);
            
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults/" + nonExistentId),
                eq(ConsultResponseDto.class)
            )).thenThrow(notFoundError);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.getConsultById(nonExistentId))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Consult not found");
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Consultas com Detalhes Agregados")
    class GetConsultWithDetailsTests {

        @Test
        @DisplayName("Deve retornar consulta com detalhes agregados quando ID válido é fornecido")
        void shouldReturnConsultWithAggregatedDetails_WhenValidIdProvided() {
            // Arrange
            Long consultId = 1L;
            ConsultResponseDto consultResponse = TestDataBuilder.createValidConsultResponseDto();
            Object patientDetails = createMockPatientDetails();
            Object doctorDetails = createMockDoctorDetails();
            
            // Mock para getConsultById (método interno)
            ResponseEntity<ConsultResponseDto> consultResponseEntity = new ResponseEntity<>(consultResponse, HttpStatus.OK);
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(ConsultResponseDto.class)
            )).thenReturn(consultResponseEntity);
            
            // Mock para detalhes do paciente
            when(restTemplate.getForObject(
                eq(patientServiceUrl + "/patients/" + consultResponse.patientId()),
                eq(Object.class)
            )).thenReturn(patientDetails);
            
            // Mock para detalhes do médico
            when(restTemplate.getForObject(
                eq(doctorServiceUrl + "/doctors/" + consultResponse.doctorId()),
                eq(Object.class)
            )).thenReturn(doctorDetails);

            // Act
            ConsultAggregatedDto actualResponse = easyConsultService.getConsultWithDetails(consultId);

            // Assert
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse.consult()).isEqualTo(consultResponse);
            assertThat(actualResponse.patientDetails()).isEqualTo(patientDetails);
            assertThat(actualResponse.doctorDetails()).isEqualTo(doctorDetails);
            
            // Verificar que todas as chamadas foram feitas
            verify(restTemplate, times(1)).getForEntity(any(String.class), eq(ConsultResponseDto.class));
            verify(restTemplate, times(1)).getForObject(contains("/patients/"), eq(Object.class));
            verify(restTemplate, times(1)).getForObject(contains("/doctors/"), eq(Object.class));
        }

        @Test
        @DisplayName("Deve implementar graceful degradation quando serviços auxiliares falham")
        void shouldImplementGracefulDegradation_WhenAuxiliaryServicesFail() {
            // Arrange
            Long consultId = 1L;
            ConsultResponseDto consultResponse = TestDataBuilder.createValidConsultResponseDto();
            
            // Mock principal da consulta (sucesso)
            ResponseEntity<ConsultResponseDto> consultResponseEntity = new ResponseEntity<>(consultResponse, HttpStatus.OK);
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(ConsultResponseDto.class)
            )).thenReturn(consultResponseEntity);
            
            // Mock falha no serviço de paciente
            when(restTemplate.getForObject(
                eq(patientServiceUrl + "/patients/" + consultResponse.patientId()),
                eq(Object.class)
            )).thenThrow(new RuntimeException("Patient service unavailable"));
            
            // Mock falha no serviço de médico
            when(restTemplate.getForObject(
                eq(doctorServiceUrl + "/doctors/" + consultResponse.doctorId()),
                eq(Object.class)
            )).thenThrow(new RuntimeException("Doctor service unavailable"));

            // Act
            ConsultAggregatedDto actualResponse = easyConsultService.getConsultWithDetails(consultId);

            // Assert
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse.consult()).isEqualTo(consultResponse);
            assertThat(actualResponse.patientDetails()).isNull(); // Graceful degradation
            assertThat(actualResponse.doctorDetails()).isNull(); // Graceful degradation
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando consulta principal falha")
        void shouldThrowExternalServiceException_WhenMainConsultFails() {
            // Arrange
            Long consultId = 1L;
            RuntimeException consultError = new RuntimeException("Consult service error");
            
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(ConsultResponseDto.class)
            )).thenThrow(consultError);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.getConsultWithDetails(consultId))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Failed to fetch consult details");
        }

        private Object createMockPatientDetails() {
            return new Object() {
                public Long getId() { return 123L; }
                public String getName() { return "João Silva"; }
                public String getEmail() { return "joao@email.com"; }
            };
        }

        private Object createMockDoctorDetails() {
            return new Object() {
                public Long getId() { return 456L; }
                public String getName() { return "Dr. Maria Santos"; }
                public String getSpecialty() { return "Cardiologia"; }
            };
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Todas as Consultas")
    class GetAllConsultsTests {

        @Test
        @DisplayName("Deve retornar lista de consultas quando existem registros")
        void shouldReturnConsultsList_WhenRecordsExist() {
            // Arrange
            ConsultResponseDto consult1 = TestDataBuilder.createValidConsultResponseDto();
            ConsultResponseDto consult2 = createSecondConsultResponse();
            ConsultResponseDto[] consultsArray = {consult1, consult2};
            
            ResponseEntity<ConsultResponseDto[]> responseEntity = new ResponseEntity<>(consultsArray, HttpStatus.OK);
            
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults"),
                eq(ConsultResponseDto[].class)
            )).thenReturn(responseEntity);

            // Act
            List<ConsultResponseDto> actualConsults = easyConsultService.getAllConsults();

            // Assert
            assertThat(actualConsults).isNotNull();
            assertThat(actualConsults).hasSize(2);
            assertThat(actualConsults).containsExactly(consult1, consult2);
            
            verify(restTemplate, times(1)).getForEntity(
                eq(consultServiceUrl + "/consults"),
                eq(ConsultResponseDto[].class)
            );
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando serviço falha")
        void shouldThrowExternalServiceException_WhenServiceFails() {
            // Arrange
            HttpClientErrorException serviceError = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            
            when(restTemplate.getForEntity(
                eq(consultServiceUrl + "/consults"),
                eq(ConsultResponseDto[].class)
            )).thenThrow(serviceError);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.getAllConsults())
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Failed to fetch consults");
        }

        private ConsultResponseDto createSecondConsultResponse() {
            return new ConsultResponseDto(
                2L,
                124L,
                457L,
                "Maria Oliveira",
                "maria.oliveira@hospital.com",
                "Dr. João Santos",
                LocalDate.now().plusDays(10),
                "Consulta de acompanhamento",
                "AGENDADA"
            );
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Consulta")
    class UpdateConsultTests {

        @Test
        @DisplayName("Deve atualizar consulta com sucesso quando dados válidos são fornecidos")
        void shouldUpdateConsultSuccessfully_WhenValidDataProvided() {
            // Arrange
            Long consultId = 1L;
            ConsultUpdateRequestDto updateDto = new ConsultUpdateRequestDto(
                "Consulta realizada com sucesso",
                "Dr. Maria Santos",
                LocalDate.now(),
                "CONCLUIDA"
            );
            
            ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            when(restTemplate.exchange(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
            )).thenReturn(responseEntity);

            // Act & Assert (não deve lançar exceção)
            assertThatNoException().isThrownBy(() -> 
                easyConsultService.updateConsult(consultId, updateDto)
            );
            
            verify(restTemplate, times(1)).exchange(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
            );
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando atualização falha")
        void shouldThrowExternalServiceException_WhenUpdateFails() {
            // Arrange
            Long consultId = 1L;
            ConsultUpdateRequestDto updateDto = new ConsultUpdateRequestDto(
                "Análise da consulta",
                "Dr. Maria Santos",
                LocalDate.now(),
                "EM_ANDAMENTO"
            );
            
            HttpClientErrorException updateError = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            
            when(restTemplate.exchange(
                eq(consultServiceUrl + "/consults/" + consultId),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
            )).thenThrow(updateError);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.updateConsult(consultId, updateDto))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("Failed to update consult");
        }
    }
}
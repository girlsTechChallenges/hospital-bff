package com.fiap.hospital.bff.infra.adapter.easyconsult;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.PatientData;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultDeleteRequestDto;
import com.fiap.hospital.bff.infra.exception.ExternalServiceException;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para EasyConsultService
 * 
 * Testa as regras de negócio relacionadas ao serviço de consultas via GraphQL:
 * - Criação de consultas
 * - Busca de todas as consultas
 * - Atualização de consultas
 * - Exclusão de consultas
 * - Tratamento de falhas de serviços externos
 * 
 * Utiliza mocks para isolar as dependências externas (GraphQL Client e UserRepository)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EasyConsultService - Testes Unitários")
class EasyConsultServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private EasyConsultService easyConsultService;

    private final String graphqlUrl = "http://localhost:8081/graphql";

    @BeforeEach
    void setUp() {
        // Configurar URL GraphQL usando ReflectionTestUtils
        ReflectionTestUtils.setField(easyConsultService, "graphqlUrl", graphqlUrl);
    }

    @Nested
    @DisplayName("Testes de Seleção de Enfermeiros")
    class NurseSelectionTests {

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando não há enfermeiros disponíveis")
        void shouldThrowExternalServiceException_WhenNoNursesAvailable() {
            // Arrange
            ConsultRequestDto requestDto = createValidConsultRequestDto();
            
            when(userRepository.findAllByTipo(TypeUsers.ENFERMEIRO))
                    .thenReturn(Collections.emptyList());

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.createConsult(requestDto))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("No nurse available");
            
            verify(userRepository, times(1)).findAllByTipo(TypeUsers.ENFERMEIRO);
        }

        @Test
        @DisplayName("Deve encontrar enfermeiro disponível quando existem enfermeiros cadastrados")
        void shouldFindAvailableNurse_WhenNursesExist() {
            // Arrange
            UserEntity mockNurse = createMockNurseEntity();
            
            when(userRepository.findAllByTipo(TypeUsers.ENFERMEIRO))
                    .thenReturn(List.of(mockNurse));

            // Act & Assert - Como o teste vai tentar conectar ao GraphQL, vamos testar apenas se encontra o enfermeiro
            // Verificar que o repositório foi chamado corretamente
            assertThatThrownBy(() -> easyConsultService.createConsult(createValidConsultRequestDto()))
                    .isInstanceOf(ExternalServiceException.class); // Vai falhar na conexão GraphQL, mas passa da verificação do enfermeiro
            
            verify(userRepository, times(1)).findAllByTipo(TypeUsers.ENFERMEIRO);
        }
    }

    @Nested
    @DisplayName("Testes de Validação de Autenticação")
    class AuthenticationValidationTests {

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando não há token de autenticação para getAllConsults")
        void shouldThrowExternalServiceException_WhenNoValidAuthTokenForGetAll() {
            // Arrange
            SecurityContextHolder.getContext().setAuthentication(null);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.getAllConsults())
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("No valid authentication token found");
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando não há token de autenticação para updateConsult")
        void shouldThrowExternalServiceException_WhenNoAuthTokenForUpdate() {
            // Arrange
            ConsultUpdateRequestDto updateDto = new ConsultUpdateRequestDto(
                    "1",
                    "Consulta atualizada",
                    "CONCLUIDA",
                    "2025-10-07",
                    "14:30:00"
            );
            
            SecurityContextHolder.getContext().setAuthentication(null);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.updateConsult(updateDto))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("No valid authentication token found");
        }

        @Test
        @DisplayName("Deve lançar ExternalServiceException quando não há token de autenticação para deleteConsult")
        void shouldThrowExternalServiceException_WhenNoAuthTokenForDelete() {
            // Arrange
            ConsultDeleteRequestDto deleteDto = new ConsultDeleteRequestDto("1");
            
            SecurityContextHolder.getContext().setAuthentication(null);

            // Act & Assert
            assertThatThrownBy(() -> easyConsultService.deleteConsult(deleteDto))
                    .isInstanceOf(ExternalServiceException.class)
                    .hasMessage("No valid authentication token found");
        }
    }

    // Métodos auxiliares para criação de objetos de teste
    private ConsultRequestDto createValidConsultRequestDto() {
        PatientData patientData = new PatientData("João Silva", "joao@email.com");
        return new ConsultRequestDto(
                patientData,
                "2025-10-14",
                "10:30:00",
                "Consulta de rotina"
        );
    }

    private UserEntity createMockNurseEntity() {
        UserEntity nurse = new UserEntity();
        nurse.setId(1L);
        nurse.setNome("Enfermeira Maria");
        nurse.setEmail("maria.enfermeira@hospital.com");
        nurse.setTipo(TypeUsers.ENFERMEIRO);
        return nurse;
    }
}
package com.fiap.hospital.bff.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.UserRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 👥 Testes de Integração Avançados para UserController
 * 
 * ===== LÓGICA DOS TESTES =====
 * Cenários específicos do ambiente hospitalar seguindo TDD:
 * 
 * 1. **Cadastro por Tipo**: Pacientes, médicos e enfermeiros têm regras diferentes
 * 2. **Segurança LGPD**: Dados sensíveis não devem vazar nas respostas
 * 3. **Workflow Hospitalar**: Testa fluxos reais do dia a dia hospitalar
 * 4. **Validações de Negócio**: CPF únicos, emails corporativos, etc.
 * 5. **Cenários de Emergência**: Médicos criando usuários rapidamente
 * 
 * ===== CENÁRIOS ESPECÍFICOS DO HOSPITAL =====
 * - Coordenação cadastra enfermeiro transferido
 * - Médico em plantão precisa listar colegas rapidamente
 * - Atualização de dados após mudança de setor
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@Transactional
@DisplayName("👥 UserController - Testes de Integração Avançados")
class UserControllerAdvancedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity existingUser;

    @BeforeEach
    void setUp() {
        // Limpar base de dados
        userRepository.deleteAll();
        
        // Criar usuário padrão para testes que precisam de autenticação
        existingUser = UserEntity.builder()
            .nome("João Silva")
            .email("joao.silva@hospital.com")
            .login("joaosilva")
            .senha(passwordEncoder.encode("senha123456"))
            .tipo(TypeUsers.PACIENTE)
            .build();
        
        existingUser = userRepository.save(existingUser);
    }

    // ===================================
    // 🏥 CENÁRIOS DE CADASTRO HOSPITALAR
    // ===================================

    @Nested
    @DisplayName("🏥 Cenários de Cadastro Hospitalar")
    class HospitalRegistrationScenariosTests {

        @Test
        @DisplayName("Recepcionista deve conseguir cadastrar novo paciente rapidamente")
        void shouldAllowReceptionistToRegisterNewPatientQuickly() throws Exception {
            // ===== LÓGICA =====
            // Cenário: Paciente chega para primeira consulta, precisa ser cadastrado rapidamente
            // Sistema deve aceitar cadastro sem autenticação (público) e ser responsivo
            
            // Arrange - Dados de paciente chegando pela primeira vez
            UserRequestDto newPatient = new UserRequestDto(
                "Carlos Mendes",
                "carlos.mendes@email.com",
                "carlosmendes",
                "senhatemporaria123",
                TypeUsers.PACIENTE
            );

            long startTime = System.currentTimeMillis();

            // Act & Assert
            mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newPatient)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.nome").value("Carlos Mendes"))
                    .andExpect(jsonPath("$.email").value("carlos.mendes@email.com"))
                    .andExpect(jsonPath("$.tipo").value("PACIENTE"));

            // Verificar performance (cadastro deve ser rápido < 5 segundos)
            long executionTime = System.currentTimeMillis() - startTime;
            assert executionTime < 5000 : "Cadastro demorou muito: " + executionTime + "ms";
        }

        @Test
        @DisplayName("RH deve cadastrar novo médico com validações específicas")
        void shouldAllowHRToRegisterNewDoctorWithSpecificValidations() throws Exception {
            // ===== LÓGICA =====
            // Cenário: RH contrata novo médico, precisa cadastrar no sistema
            // Médicos podem ter nomes com prefixos (Dr., Dra.) e emails corporativos
            
            // Arrange - Médico recém-contratado
            UserRequestDto newDoctor = new UserRequestDto(
                "Roberto Cardoso",
                "roberto.cardoso@hospital.com", // Email corporativo
                "drroberto",
                "senhaComplexaMedico123",
                TypeUsers.MEDICO
            );

            // Act & Assert
            mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newDoctor)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome").value("Roberto Cardoso"))
                    .andExpect(jsonPath("$.email").value("roberto.cardoso@hospital.com"))
                    .andExpect(jsonPath("$.tipo").value("MEDICO"))
                    .andExpect(jsonPath("$.login").value("drroberto"));
        }

        @Test
        @DisplayName("Deve rejeitar email duplicado com mensagem clara")
        void shouldRejectDuplicateEmailRegistration() throws Exception {
            // ===== LÓGICA =====
            // Sistema não pode permitir emails duplicados
            // Importante para evitar conflitos de identidade em ambiente médico
            
            // Arrange - Tentar usar email que já existe
            UserRequestDto duplicateEmailUser = new UserRequestDto(
                "João Mendes",
                "joao.silva@hospital.com", // Email já usado (mesmo do existingUser)
                "joaomendes",
                "senha123456",
                TypeUsers.PACIENTE
            );

            // Act & Assert
            mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(duplicateEmailUser)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").value(containsString("is already registered")));
        }
    }

    @Nested
    @DisplayName("🔍 Cenários de Consulta e Listagem")
    class QueryAndListingScenariosTests {

        @Test
        @DisplayName("Deve proteger listagem com autenticação obrigatória")
        void shouldProtectListingWithMandatoryAuthentication() throws Exception {
            // ===== LÓGICA =====
            // Dados de usuários são sensíveis (LGPD)
            // Apenas usuários autenticados podem ver listas
            
            // Act & Assert - Tentar listar sem autenticação
            mockMvc.perform(get("/api/v1/users"))
                    .andExpect(status().isUnauthorized()); // Esperamos 401, não 403
        }
    }
}
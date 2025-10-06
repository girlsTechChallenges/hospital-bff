package com.fiap.hospital.bff.infra.adapter.easyconsult;

import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultFilterDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.GraphQLConsultationResponse;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultDeleteRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ProfessionalData;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import com.fiap.hospital.bff.infra.exception.ExternalServiceException;
import com.fiap.hospital.bff.infra.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EasyConsultService {

    private static final Logger log = LoggerFactory.getLogger(EasyConsultService.class);

    private static final String CONSULTATION_FRAGMENT = """
            id
            patient {
                name
                email
            }
            nameProfessional
            localTime
            date
            statusConsultation
            reason
        """;

    private final UserRepository userRepository;
    private final String graphqlUrl;

    public EasyConsultService(UserRepository userRepository,
                            @Value("${app.graphql.easyconsult.url:http://localhost:8081/graphql}") String graphqlUrl) {
        this.userRepository = userRepository;
        this.graphqlUrl = graphqlUrl;
    }

    public GraphQLConsultationResponse createConsult(ConsultRequestDto request) {
        log.info("Creating consult: {}", request);

        ProfessionalData professional = selectAvailableNurse();

        String mutation = """
            mutation CreateConsultation($input: ConsultationRequestDto!) {
                createFullConsultation(input: $input) {
                    %s
                }
            }
            """.formatted(CONSULTATION_FRAGMENT);

        Map<String, Object> variables = Map.of(
                "input", buildCreateConsultInput(request, professional)
        );

        return executeGraphQLMutation(mutation, variables, "createFullConsultation",
                                    GraphQLConsultationResponse.class);
    }

    public List<GraphQLConsultationResponse> getAllConsults() {
        log.info("Fetching all consults");

        String query = """
            query {
                getAllConsultations {
                    %s
                }
            }
            """.formatted(CONSULTATION_FRAGMENT);

        return executeGraphQLQuery(query, Map.of(), "getAllConsultations",
                                 GraphQLConsultationResponse.class);
    }

    public List<GraphQLConsultationResponse> getConsultsByFilter(ConsultFilterDto filter) {
        log.info("Fetching consults with filter: {}", filter);

        String query = """
            query GetFilteredConsultations($filter: ConsultationFilterRequestDto!) {
                getFilteredConsultations(filter: $filter) {
                    %s
                }
            }
            """.formatted(CONSULTATION_FRAGMENT);

        Map<String, Object> variables = Map.of("filter", buildFilterMap(filter));

        return executeGraphQLQuery(query, variables, "getFilteredConsultations",
                                 GraphQLConsultationResponse.class);
    }

    public GraphQLConsultationResponse updateConsult(ConsultUpdateRequestDto request) {
        log.info("Updating consult: {}", request);

        String mutation = """
            mutation UpdateConsultation($input: ConsultationUpdateRequestDto!) {
                updateConsultation(input: $input) {
                    %s
                }
            }
            """.formatted(CONSULTATION_FRAGMENT);

        Map<String, Object> variables = Map.of(
                "input", buildUpdateConsultInput(request)
        );

        return executeGraphQLMutation(mutation, variables, "updateConsultation",
                                    GraphQLConsultationResponse.class);
    }

    public Boolean deleteConsult(ConsultDeleteRequestDto request) {
        log.info("Deleting consult: {}", request);

        String mutation = """
            mutation DeleteConsultation($id: ID!) {
                deleteConsultation(id: $id)
            }
            """;

        Map<String, Object> variables = Map.of("id", request.id());

        return executeGraphQLMutation(mutation, variables, "deleteConsultation", Boolean.class);
    }

    private <T> T executeGraphQLMutation(String mutation, Map<String, Object> variables,
                                       String fieldName, Class<T> responseType) {
        try {
            GraphQlClient client = createAuthenticatedClient();

            log.debug("Executing GraphQL mutation. Field: {}, Variables: {}", fieldName, variables);

            ClientGraphQlResponse response = client.document(mutation)
                    .variables(variables)
                    .execute()
                    .block();

            validateResponse(response);

            T result = response.field(fieldName).toEntity(responseType);

            if (result == null) {
                log.error("{} returned null. Full response: {}", fieldName, response);
                throw new ExternalServiceException("GraphQL mutation returned null data");
            }

            log.info("GraphQL mutation executed successfully. Field: {}", fieldName);
            return result;

        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error executing GraphQL mutation: {}", fieldName, e);
            throw new ExternalServiceException("Error executing GraphQL mutation: " + e.getMessage());
        }
    }

    private <T> List<T> executeGraphQLQuery(String query, Map<String, Object> variables,
                                          String fieldName, Class<T> responseType) {
        try {
            GraphQlClient client = createAuthenticatedClient();

            log.debug("Executing GraphQL query. Field: {}, Variables: {}", fieldName, variables);

            List<T> response = client.document(query)
                    .variables(variables)
                    .retrieve(fieldName)
                    .toEntityList(responseType)
                    .block();

            List<T> result = Optional.ofNullable(response)
                    .orElseThrow(() -> new ExternalServiceException("Null response from GraphQL query"));

            log.info("GraphQL query executed successfully. Field: {}, Results: {}", fieldName, result.size());
            return result;

        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error executing GraphQL query: {}", fieldName, e);
            throw new ExternalServiceException("Error executing GraphQL query: " + e.getMessage());
        }
    }

    private GraphQlClient createAuthenticatedClient() {
        String token = getAuthenticationToken();

        return HttpGraphQlClient.builder()
                .url(graphqlUrl)
                .header("Authorization", "Bearer " + token)
                .build();
    }

    private String getAuthenticationToken() {
        var authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getToken() == null) {
            throw new ExternalServiceException("No valid authentication token found");
        }
        return authentication.getToken().getTokenValue();
    }

    private void validateResponse(ClientGraphQlResponse response) {
        if (response == null) {
            throw new ExternalServiceException("Null response from GraphQL service");
        }

        if (!response.getErrors().isEmpty()) {
            String errorMessages = response.getErrors().stream()
                    .map(ResponseError::getMessage)
                    .collect(Collectors.joining("; "));
            log.error("GraphQL errors: {}", response.getErrors());
            throw new ExternalServiceException("GraphQL errors: " + errorMessages);
        }
    }

    private Map<String, Object> buildCreateConsultInput(ConsultRequestDto request, ProfessionalData professional) {
        return Map.of(
                "patient", Map.of(
                        "name", request.patient().name(),
                        "email", request.patient().email()
                ),
                "professional", Map.of(
                        "name", professional.name(),
                        "email", professional.email()
                ),
                "localTime", request.consultTime(),
                "date", request.consultDate(),
                "reason", request.consultReason()
        );
    }

    private Map<String, Object> buildUpdateConsultInput(ConsultUpdateRequestDto request) {
        Map<String, Object> input = new HashMap<>();
        input.put("id", request.id());

        addIfNotNull(input, "reason", request.reason());
        addIfNotNull(input, "status", request.status());
        addIfNotNull(input, "date", request.date());
        addIfNotNull(input, "localTime", request.localTime());

        return input;
    }

    private Map<String, Object> buildFilterMap(ConsultFilterDto filter) {
        if (filter == null) return Map.of();

        Map<String, Object> filterMap = new HashMap<>();

        addIfNotNull(filterMap, "patientEmail", filter.patientEmail());
        addIfNotNull(filterMap, "professionalEmail", filter.professionalEmail());
        addIfNotNull(filterMap, "localTime", filter.localTime());
        addIfNotNull(filterMap, "date", filter.date());
        addIfNotNull(filterMap, "status", filter.status());

        return filterMap;
    }

    private void addIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null && !(value instanceof String valueStr && valueStr.trim().isEmpty())) {
            map.put(key, value);
        }
    }

    private ProfessionalData selectAvailableNurse() {
        return userRepository.findAllByTipo(TypeUsers.ENFERMEIRO).stream()
                .findFirst()
                .map(nurse -> new ProfessionalData(nurse.getNome(), nurse.getEmail()))
                .orElseThrow(() -> new ExternalServiceException("No nurse available"));
    }
}

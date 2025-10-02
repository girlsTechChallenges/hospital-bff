package com.fiap.hospital.bff.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private final OpenApiConfig openApiConfig = new OpenApiConfig();

    @Test
    void testCustomOpenAPI_notNull() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        assertNotNull(openAPI);
    }

    @Test
    void testOpenAPIInfo() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        assertNotNull(info);
        assertEquals("Hospital BFF API", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("API para gerenciamento do Hospital", info.getDescription());

        Contact contact = info.getContact();
        assertNotNull(contact);
        assertEquals("Equipe Girls Tech Challenges", contact.getName());
        assertEquals("contato@girlstechchallenges.com", contact.getEmail());
    }

    @Test
    void testOpenAPIComponentsResponses() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        Components components = openAPI.getComponents();

        assertNotNull(components);

        ApiResponse response200 = components.getResponses().get("200");
        assertNotNull(response200);
        assertEquals("Success", response200.getDescription());

        ApiResponse response202 = components.getResponses().get("202");
        assertNotNull(response202);
        assertEquals("Accepted", response202.getDescription());

        ApiResponse response204 = components.getResponses().get("204");
        assertNotNull(response204);
        assertEquals("No Content", response204.getDescription());

        ApiResponse response400 = components.getResponses().get("400");
        assertNotNull(response400);
        assertEquals("Bad Request", response400.getDescription());

        ApiResponse response404 = components.getResponses().get("404");
        assertNotNull(response404);
        assertEquals("Not Found", response404.getDescription());

        ApiResponse response409 = components.getResponses().get("409");
        assertNotNull(response409);
        assertEquals("Conflict", response409.getDescription());

        ApiResponse response500 = components.getResponses().get("500");
        assertNotNull(response500);
        assertEquals("Internal Server Error", response500.getDescription());
    }
}

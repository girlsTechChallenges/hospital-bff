package com.fiap.hospital.bff.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital API")
                        .version("1.0.0")
                        .description("API para gerenciamento do Hospital")
                        .contact(new Contact()
                                .name("Equipe Girls Tech Challenges")
                                .email("contato@girlstechchallenges.com")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Hospital API - Servidor Local (Desenvolvimento)"),
                        new Server()
                                .url("http://localhost:8000")
                                .description("Hospital API - Kong Gateway (API Gateway)"),
                        new Server()
                                .url("https://api.hospital.com")
                                .description("Hospital API - Produção (HTTPS)")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                        .addResponses("200", new ApiResponse().description("Success"))
                        .addResponses("202", new ApiResponse().description("Accepted"))
                        .addResponses("204", new ApiResponse().description("No Content"))
                        .addResponses("400", new ApiResponse().description("Bad Request"))
                        .addResponses("404", new ApiResponse().description("Not Found"))
                        .addResponses("409", new ApiResponse().description("Conflict"))
                        .addResponses("500", new ApiResponse().description("Internal Server Error"))
                );
    }
}
package com.fiap.hospital.bff.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .components(new Components()
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

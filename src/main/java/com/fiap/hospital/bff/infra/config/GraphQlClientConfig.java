package com.fiap.hospital.bff.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlClientConfig {

    @Bean
    public GraphQlClient graphQlClient() {
        WebClient webClient = WebClient.builder().build();

        return HttpGraphQlClient.builder(webClient)
                .url("http://localhost:8081/graphql")
                .build();
    }
}

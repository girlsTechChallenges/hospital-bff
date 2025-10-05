package com.fiap.hospital.bff.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalServicesHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;

    @Value("${external.services.consult.url}")
    private String consultServiceUrl;

    public ExternalServicesHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            restTemplate.getForEntity(consultServiceUrl + "/health", String.class);
            return Health.up()
                .withDetail("consult-service", "Available")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("consult-service", "Unavailable")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}

package com.fiap.hospital.bff.infra.entrypoint.dto.graphql;

public record ConsultFilterDto(
        String patientEmail,
        String professionalEmail,
        String localTime,
        String date,
        String status
) {}

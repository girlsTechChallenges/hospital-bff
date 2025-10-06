package com.fiap.hospital.bff.infra.entrypoint.dto.graphql;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GraphQLConsultationResponse(
        String id,
        PatientResponse patient,
        String nameProfessional,
        String localTime,
        String date,
        String statusConsultation,
        String reason
) {

    public record PatientResponse(
            String name,
            String email
    ) {}
}
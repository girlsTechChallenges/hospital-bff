package com.fiap.hospital.bff.infra.entrypoint.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ConsultAggregatedDto(
    ConsultResponseDto consult,
    Object patientDetails,
    Object doctorDetails
) {}

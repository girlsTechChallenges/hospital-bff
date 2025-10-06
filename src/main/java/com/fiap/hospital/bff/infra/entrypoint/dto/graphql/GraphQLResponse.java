package com.fiap.hospital.bff.infra.entrypoint.dto.graphql;

import java.util.List;

public record GraphQLResponse<T>(
        T data,
        List<GraphQLError> errors
) {}


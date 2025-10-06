package com.fiap.hospital.bff.infra.entrypoint.controller.docs;

import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.GraphQLConsultationResponse;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.graphql.ConsultDeleteRequestDto;
import com.fiap.hospital.bff.infra.exception.ApiErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "EasyConsult", description = "API endpoints for user consult registration and management")
public interface EasyConsultControllerDocs {

    @Operation(summary = "Create consult", description = "Allows a user to create a new consult")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "201", description = "User registered successfully.",
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
              @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
              @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
              @ApiResponse(responseCode = "502", description = "Bad Gateway",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<GraphQLConsultationResponse> create(@Valid @RequestBody ConsultRequestDto consultaRequest);

    @Operation(summary = "Get all consults", description = "Retrieve a list of all registered consults.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Consults retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized.",
                        content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
                @ApiResponse(responseCode = "500", description = "Internal server error.",
                        content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
                @ApiResponse(responseCode = "502", description = "Bad Gateway",
                        content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<List<GraphQLConsultationResponse>> getAll();

    @Operation(summary = "Get consults by filter", description = "Retrieve consults filtered by patient email, professional email, local time, date, and status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consults retrieved successfully.",
                content = @Content(schema = @Schema(implementation = GraphQLConsultationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<List<GraphQLConsultationResponse>> getByFilter(
            @Parameter(description = "Patient email filter") @RequestParam(required = false) String patientEmail,
            @Parameter(description = "Professional email filter") @RequestParam(required = false) String professionalEmail,
            @Parameter(description = "Local time filter") @RequestParam(required = false) String localTime,
            @Parameter(description = "Date filter") @RequestParam(required = false) String date,
            @Parameter(description = "Status filter") @RequestParam(required = false) String status);

    @Operation(summary = "Update consult", description = "Update an existing consult")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consult updated successfully.",
                content = @Content(schema = @Schema(implementation = GraphQLConsultationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Consult not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<GraphQLConsultationResponse> update(@Valid @RequestBody ConsultUpdateRequestDto updateRequest);

    @Operation(summary = "Delete consult", description = "Delete an existing consult")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consult deleted successfully.",
                content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Consult not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<Boolean> delete(@Valid @RequestBody ConsultDeleteRequestDto deleteRequest);
}

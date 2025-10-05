package com.fiap.hospital.bff.infra.entrypoint.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.request.ConsultUpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.ConsultResponseDto;
import com.fiap.hospital.bff.infra.exception.ApiErrorMessage;

@Tag(name = "EasyConsult", description = "API endpoints for user consult registration and management")
public interface EasyConsultControllerDocs {

    @Operation(summary = "Create consult", description = "Allows a user to create a new consult")
      @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully.", 
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field: mandatory criteria were not met.", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "User already registered.", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<ConsultResponseDto> create(@Valid @RequestBody ConsultRequestDto consultaRequest);

    @Operation(summary = "Get consult by ID", description = "Allows a user to retrieve a consult by ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consult found successfully.", 
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "Consult not found.", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<ConsultResponseDto> getById(@PathVariable @NotNull Long id);

    @Operation(summary = "Get all consults by patient ID", description = "Allows a user to retrieve all consults by patient ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consults found successfully.", 
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "Consults not found.", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<List<ConsultResponseDto>> getByPatientId(@PathVariable @NotNull Long patientId);

    @Operation(summary = "Get all consults", description = "Retrieve a list of all registered consults.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consults retrieved successfully.", 
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<List<ConsultResponseDto>> getAll();

    @Operation(summary = "Update consult", description = "Update an existing consult.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consult updated successfully.", 
                    content = @Content(schema = @Schema(implementation = ConsultRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field: mandatory criteria were not met.", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", 
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<ConsultResponseDto> update(@PathVariable @NotNull Long id, @RequestBody @Valid ConsultUpdateRequestDto consultUpdateRequest);
}


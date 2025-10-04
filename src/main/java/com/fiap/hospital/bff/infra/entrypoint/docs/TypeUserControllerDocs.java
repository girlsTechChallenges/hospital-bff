package com.fiap.hospital.bff.infra.entrypoint.docs;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.dto.response.TypeResponseDto;
import com.fiap.hospital.bff.infra.exception.ApiErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "User Type Management", description = "Endpoints for managing user types.")
public interface TypeUserControllerDocs {

    @Operation(summary = "Create User Type", description = "Registers a new user type in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type successfully created.",
                    content = @Content(schema = @Schema(implementation = TypeResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User type already exists.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<TypeResponseDto> create(@Valid @RequestBody TypeRequestDto request);

    @Operation(summary = "List User Types", description = "Retrieves all registered user types.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User types successfully retrieved.",
                    content = @Content(schema = @Schema(implementation = TypeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<List<TypeResponseDto>> getAll();

    @Operation(summary = "Get User Type by ID", description = "Fetches a user type by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type successfully retrieved.",
                    content = @Content(schema = @Schema(implementation = TypeResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User type not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<TypeResponseDto> getById(@PathVariable @NotNull Long id);
}

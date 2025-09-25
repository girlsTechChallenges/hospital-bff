package com.fiap.hospital.bff.infra.entrypoint.controller.docs;

import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UpdateRequestDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserDto;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.response.UserResponseDto;
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


@Tag(name = "User Management", description = "API endpoints for user management operations")
public interface UserControllerDocs {

    @Operation(summary = "Create a user", description = "Register a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully.",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field: mandatory criteria were not met.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "User already registered.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserDto userRequestDto);

    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<List<UserResponseDto>> getAll();

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully.",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<UserResponseDto> getById(@PathVariable @NotNull Long id);

    @Operation(summary = "Update user", description = "Update an existing user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully.",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid field: mandatory criteria were not met.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<UserResponseDto> update(@PathVariable @NotNull Long id, @RequestBody @Valid UpdateRequestDto updateRequestDto);

    @Operation(summary = "Delete user", description = "Delete a user from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Conflict.",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorMessage.class)))
    })
    ResponseEntity<Void> delete(@PathVariable @NotNull Long id);
}

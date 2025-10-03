package com.fiap.hospital.bff.infra.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(fieldError -> Objects.requireNonNull(fieldError.getDefaultMessage()), Collectors.toList())
                ));

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(apiErrorMessage.getStatus()).body(apiErrorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorMessage> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {

        Map<String, List<String>> errors = new HashMap<>();
        errors.put("message", List.of(exception.getMessage()));

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, errors);
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AuthorizationDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Access Denied");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(UserCredentialsException.class)
    public ResponseEntity<Map<String, String>> userCredentialsException(UserCredentialsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid email or password");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, String>> userAlreadyRegisteredException(UserAlreadyRegisteredException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid email or password");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }



    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Map<String, String>> userTypeMismatchException(TypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Mismatch Exception");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(TypeAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, String>> userTypeMismatchException(TypeAlreadyRegisteredException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Mismatch Exception");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }


    @ExceptionHandler(TypeNotFoundException.class)
    public ResponseEntity<ApiErrorMessage> typeNotFoundException(TypeNotFoundException exception, WebRequest request) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("message", List.of(exception.getMessage()));
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, errors);
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
package com.example.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Error body returned by PetClinic ExceptionControllerAdvice (RFC 7807 ProblemDetail).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProblemDetailResponse(
        String title,
        String detail,
        Integer status,
        List<SchemaValidationError> schemaValidationErrors
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SchemaValidationError(String message) {
    }
}

package com.example.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Тело ошибки валидации PetClinic (RFC 7807 ProblemDetail).
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>вложенная Pydantic-модель → вложенный {@code record SchemaValidationError}</li>
 *   <li>{@code list[Error]} → {@code List<SchemaValidationError>}</li>
 * </ul>
 *
 * <p>Источник на стороне SUT: ExceptionControllerAdvice (MethodArgumentNotValidException → 400).
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

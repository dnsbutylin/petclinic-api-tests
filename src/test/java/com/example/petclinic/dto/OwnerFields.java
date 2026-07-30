package com.example.petclinic.dto;

/**
 * Request body for POST/PUT /api/owners (аналог Pydantic-модели в Python-проекте).
 */
public record OwnerFields(
        String firstName,
        String lastName,
        String address,
        String city,
        String telephone
) {
}

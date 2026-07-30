package com.example.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response body for owner endpoints.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OwnerResponse(
        Integer id,
        String firstName,
        String lastName,
        String address,
        String city,
        String telephone
) {
}

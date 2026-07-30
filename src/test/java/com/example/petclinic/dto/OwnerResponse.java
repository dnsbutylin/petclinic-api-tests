package com.example.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Ответ API по owner (есть id).
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>{@code Model.model_validate(response.json())} → {@code response.as(OwnerResponse.class)}</li>
 *   <li>{@code model_config = ConfigDict(extra="ignore")} → {@code @JsonIgnoreProperties(ignoreUnknown = true)}</li>
 * </ul>
 *
 * <p>ignoreUnknown нужен, потому что PetClinic может вернуть ещё pets и др. поля — нам они не нужны в assert'ах.
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

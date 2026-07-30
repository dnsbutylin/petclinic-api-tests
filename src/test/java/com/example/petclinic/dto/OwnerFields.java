package com.example.petclinic.dto;

/**
 * Тело запроса POST/PUT /api/owners.
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>Pydantic {@code class OwnerFields(BaseModel)} → {@code record OwnerFields(...)}</li>
 *   <li>{@code model_dump()} / json → Jackson сериализует record в JSON (RestAssured {@code .body(...)})</li>
 *   <li>поля immutable после создания (как frozen model)</li>
 * </ul>
 *
 * <p>Поля совпадают с OpenAPI PetClinic: firstName, lastName, address, city, telephone.
 * telephone в API — ровно 10 цифр.
 */
public record OwnerFields(
        String firstName,
        String lastName,
        String address,
        String city,
        String telephone
) {
}

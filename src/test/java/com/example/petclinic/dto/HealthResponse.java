package com.example.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Ответ GET /actuator/health.
 *
 * <p>Реальный JSON: {@code {"status":"UP","groups":[...]}}. Берём только status.
 * Python: {@code class Health(BaseModel): status: str} + extra ignore.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record HealthResponse(String status) {
}

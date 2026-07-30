package com.example.petclinic.tests;

import com.example.petclinic.client.HealthClient;
import com.example.petclinic.config.ApiTestBase;
import com.example.petclinic.dto.HealthResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("PetClinic API")
@Feature("Health")
@DisplayName("Health check")
class HealthCheckTest extends ApiTestBase {

    private HealthClient healthClient;

    @BeforeEach
    void setUp() {
        healthClient = new HealthClient(requestSpec);
    }

    @Test
    @Story("Actuator health is UP")
    @DisplayName("GET /actuator/health возвращает 200 и status=UP")
    void healthShouldBeUp() {
        Response response = healthClient.getHealth();

        assertThat(response.statusCode()).isEqualTo(200);

        HealthResponse body = healthClient.asHealth(response);
        assertThat(body.status()).isEqualTo("UP");
    }
}

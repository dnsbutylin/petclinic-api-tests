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

/**
 * ТЗ п.1 Health check: GET /actuator/health → 200 и status == UP.
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>{@code def test_...( ):} → {@code @Test void ...()}</li>
 *   <li>{@code assert response.status_code == 200} → {@code assertThat(...).isEqualTo(200)}</li>
 *   <li>наследование fixture-базы → {@code extends ApiTestBase}</li>
 *   <li>Allure epic/feature → {@code @Epic}/{@code @Feature}/{@code @Story}</li>
 * </ul>
 */
@Epic("PetClinic API")
@Feature("Health")
@DisplayName("Health check")
class HealthCheckTest extends ApiTestBase {

    private HealthClient healthClient;

    @BeforeEach
    void setUp() {
        // как создание клиента в setup/fixture
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

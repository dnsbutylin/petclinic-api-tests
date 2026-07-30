package com.example.petclinic.client;

import com.example.petclinic.dto.HealthResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * HTTP-клиент health.
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>{@code clients/health_client.py} → этот класс</li>
 *   <li>{@code requests.get(url)} / httpx → RestAssured {@code given().when().get(...)}</li>
 *   <li>Allure step / декоратор step → {@code @Step}</li>
 * </ul>
 *
 * <p>Клиент НЕ делает assert — только запрос и парсинг. Проверки живут в тестах.
 */
public class HealthClient {

    private final RequestSpecification requestSpec;

    public HealthClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("GET /actuator/health")
    public Response getHealth() {
        // given = подготовка, when = вызов, then().extract() = забрать Response без assert
        return given()
                .spec(requestSpec)
                .when()
                .get("/actuator/health")
                .then()
                .extract()
                .response();
    }

    @Step("Parse health response")
    public HealthResponse asHealth(Response response) {
        // JSON → record (как model_validate)
        return response.as(HealthResponse.class);
    }
}

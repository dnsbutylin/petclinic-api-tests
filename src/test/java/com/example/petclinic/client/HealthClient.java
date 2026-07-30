package com.example.petclinic.client;

import com.example.petclinic.dto.HealthResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class HealthClient {

    private final RequestSpecification requestSpec;

    public HealthClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("GET /actuator/health")
    public Response getHealth() {
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
        return response.as(HealthResponse.class);
    }
}

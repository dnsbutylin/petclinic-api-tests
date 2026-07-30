package com.example.petclinic.client;

import com.example.petclinic.dto.OwnerFields;
import com.example.petclinic.dto.OwnerResponse;
import com.example.petclinic.dto.ProblemDetailResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OwnersClient {

    private final RequestSpecification requestSpec;

    public OwnersClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("POST /api/owners")
    public Response createOwner(OwnerFields owner) {
        return given()
                .spec(requestSpec)
                .body(owner)
                .when()
                .post("/api/owners")
                .then()
                .extract()
                .response();
    }

    @Step("GET /api/owners/{ownerId}")
    public Response getOwner(int ownerId) {
        return given()
                .spec(requestSpec)
                .when()
                .get("/api/owners/{ownerId}", ownerId)
                .then()
                .extract()
                .response();
    }

    @Step("PUT /api/owners/{ownerId}")
    public Response updateOwner(int ownerId, OwnerFields owner) {
        return given()
                .spec(requestSpec)
                .body(owner)
                .when()
                .put("/api/owners/{ownerId}", ownerId)
                .then()
                .extract()
                .response();
    }

    @Step("DELETE /api/owners/{ownerId}")
    public Response deleteOwner(int ownerId) {
        return given()
                .spec(requestSpec)
                .when()
                .delete("/api/owners/{ownerId}", ownerId)
                .then()
                .extract()
                .response();
    }

    @Step("Parse owner response")
    public OwnerResponse asOwner(Response response) {
        return response.as(OwnerResponse.class);
    }

    @Step("Parse problem detail response")
    public ProblemDetailResponse asProblem(Response response) {
        return response.as(ProblemDetailResponse.class);
    }
}

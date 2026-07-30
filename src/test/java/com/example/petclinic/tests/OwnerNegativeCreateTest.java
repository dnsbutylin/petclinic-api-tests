package com.example.petclinic.tests;

import com.example.petclinic.client.OwnersClient;
import com.example.petclinic.config.ApiTestBase;
import com.example.petclinic.dto.OwnerFields;
import com.example.petclinic.dto.ProblemDetailResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("PetClinic API")
@Feature("Owners")
@DisplayName("Owner negative create")
class OwnerNegativeCreateTest extends ApiTestBase {

    private OwnersClient ownersClient;

    @BeforeEach
    void setUp() {
        ownersClient = new OwnersClient(requestSpec);
    }

    @Test
    @Story("Validation error on empty required fields")
    @DisplayName("POST /api/owners с пустыми обязательными полями → 400 + ProblemDetail")
    void createOwnerWithEmptyRequiredFieldsShouldFail() {
        OwnerFields invalidOwner = new OwnerFields(
                "",
                "",
                "",
                "",
                ""
        );

        Response response = ownersClient.createOwner(invalidOwner);

        assertThat(response.statusCode()).isEqualTo(400);

        ProblemDetailResponse problem = ownersClient.asProblem(response);
        assertThat(problem.status()).isEqualTo(400);
        assertThat(problem.title()).isNotBlank();
        assertThat(problem.detail()).isNotBlank();
        assertThat(problem.schemaValidationErrors())
                .as("ожидаем список ошибок валидации полей")
                .isNotNull()
                .isNotEmpty();
    }
}

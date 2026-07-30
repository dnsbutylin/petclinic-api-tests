package com.example.petclinic.tests;

import com.example.petclinic.client.OwnersClient;
import com.example.petclinic.config.ApiTestBase;
import com.example.petclinic.dto.OwnerFields;
import com.example.petclinic.dto.OwnerResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ТЗ п.2 CRUD-flow owner:
 * POST → GET → PUT → DELETE → GET(после delete).
 *
 * <p>Ожидания из ТЗ:
 * создался → доступен по id → после update данные изменены → после delete недоступен.
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>один сценарий-flow в одном тесте (как один test_* с несколькими шагами)</li>
 *   <li>{@code assert a == b} → AssertJ {@code assertThat(a).isEqualTo(b)}</li>
 *   <li>payload dict/Pydantic → {@code new OwnerFields(...)}</li>
 * </ul>
 *
 * <p>Важно по реальному PetClinic: PUT и DELETE отдают 204 No Content,
 * поэтому «данные изменились» проверяем повторным GET, а не телом PUT.
 */
@Epic("PetClinic API")
@Feature("Owners")
@DisplayName("Owner CRUD flow")
class OwnerCrudFlowTest extends ApiTestBase {

    private OwnersClient ownersClient;

    @BeforeEach
    void setUp() {
        ownersClient = new OwnersClient(requestSpec);
    }

    @Test
    @Story("Full owner lifecycle")
    @DisplayName("CRUD: create → get → update → delete → get (404)")
    void ownerCrudFlow() {
        OwnerFields createPayload = new OwnerFields(
                "Ivan",
                "Petrov",
                "Lenina 1",
                "Moscow",
                "1234567890" // ровно 10 цифр — требование API
        );

        // CREATE
        Response createResponse = ownersClient.createOwner(createPayload);
        assertThat(createResponse.statusCode()).isEqualTo(201);

        OwnerResponse created = ownersClient.asOwner(createResponse);
        assertThat(created.id()).isNotNull();
        assertThat(created.firstName()).isEqualTo(createPayload.firstName());
        assertThat(created.lastName()).isEqualTo(createPayload.lastName());
        assertThat(created.address()).isEqualTo(createPayload.address());
        assertThat(created.city()).isEqualTo(createPayload.city());
        assertThat(created.telephone()).isEqualTo(createPayload.telephone());

        int ownerId = created.id();

        // READ после create
        Response getAfterCreate = ownersClient.getOwner(ownerId);
        assertThat(getAfterCreate.statusCode()).isEqualTo(200);
        OwnerResponse fetched = ownersClient.asOwner(getAfterCreate);
        assertThat(fetched.id()).isEqualTo(ownerId);
        assertThat(fetched.firstName()).isEqualTo("Ivan");
        assertThat(fetched.lastName()).isEqualTo("Petrov");

        // UPDATE
        OwnerFields updatePayload = new OwnerFields(
                "Ivan",
                "Sidorov",
                "Pushkina 10",
                "SPb",
                "0987654321"
        );
        Response updateResponse = ownersClient.updateOwner(ownerId, updatePayload);
        assertThat(updateResponse.statusCode()).isEqualTo(204);

        Response getAfterUpdate = ownersClient.getOwner(ownerId);
        assertThat(getAfterUpdate.statusCode()).isEqualTo(200);
        OwnerResponse updated = ownersClient.asOwner(getAfterUpdate);
        assertThat(updated.firstName()).isEqualTo(updatePayload.firstName());
        assertThat(updated.lastName()).isEqualTo(updatePayload.lastName());
        assertThat(updated.address()).isEqualTo(updatePayload.address());
        assertThat(updated.city()).isEqualTo(updatePayload.city());
        assertThat(updated.telephone()).isEqualTo(updatePayload.telephone());

        // DELETE + проверка, что больше недоступен
        Response deleteResponse = ownersClient.deleteOwner(ownerId);
        assertThat(deleteResponse.statusCode()).isEqualTo(204);

        Response getAfterDelete = ownersClient.getOwner(ownerId);
        assertThat(getAfterDelete.statusCode()).isEqualTo(404);
    }
}

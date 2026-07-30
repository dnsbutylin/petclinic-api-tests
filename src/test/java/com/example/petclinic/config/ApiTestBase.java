package com.example.petclinic.config;

import com.example.petclinic.PetclinicApiTestsApplication;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * База всех API-тестов.
 *
 * <p>Шпаргалка Python → Java:
 * <ul>
 *   <li>{@code conftest.py} + fixtures → этот класс ({@code extends ApiTestBase})</li>
 *   <li>{@code os.getenv("BASE_URL")} / pytest ini → {@code application.properties} + {@code -DbaseUrl}</li>
 *   <li>общий httpx/requests session → {@link RequestSpecification} ({@code requestSpec})</li>
 *   <li>{@code @pytest.fixture(autouse=True)} на каждый тест → {@code @BeforeEach}</li>
 * </ul>
 *
 * <p>Важно: мы НЕ поднимаем PetClinic здесь. PetClinic — отдельный процесс (Docker / spring-boot:run).
 * Spring Boot в тестах нужен только для свойств/контекста ({@code webEnvironment = NONE}).
 */
@SpringBootTest(
        classes = PetclinicApiTestsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public abstract class ApiTestBase {

    /** Общая «сессия» RestAssured: JSON + логи + Allure. Аналог session/client в Python. */
    protected RequestSpecification requestSpec;

    /** Дефолт из application.properties (ключ base-url). Как значение по умолчанию в .env */
    @Value("${base-url}")
    private String configuredBaseUrl;

    @BeforeEach
    void configureRestAssured() {
        // VPN/системный proxy иногда ломает доступ к 127.0.0.1 — сбрасываем на всякий случай
        System.clearProperty("socksProxyHost");
        System.clearProperty("socksProxyPort");
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("https.proxyHost");
        System.clearProperty("https.proxyPort");
        System.setProperty("java.net.useSystemProxies", "false");

        // Приоритет как в задании: -DbaseUrl=... перекрывает properties
        String baseUrl = System.getProperty("baseUrl");
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = configuredBaseUrl;
        }

        RestAssured.reset();
        RestAssured.baseURI = baseUrl; // дальше пути вида "/api/owners" клеятся к этому base
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())       // плюс из ТЗ: вложения в Allure
                .addFilter(new RequestLoggingFilter())   // плюс из ТЗ: request logging
                .addFilter(new ResponseLoggingFilter())  // плюс из ТЗ: response logging
                .build();
    }
}

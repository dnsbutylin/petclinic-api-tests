# PetClinic API Tests (Java)

Отдельный проект API-автотестов для [Spring PetClinic REST](https://github.com/spring-petclinic/spring-petclinic-rest).  
Структура близка к Python-референсу [`back_autotest_pydantic`](https://github.com/dnsbutylin/back_autotest_pydantic): clients / dto / tests + Allure.

## Стек

| Что | Версия / инструмент |
|-----|---------------------|
| Java | 17+ (проверено на 22) |
| Maven | 3.9+ (есть Maven Wrapper: `mvnw` / `mvnw.cmd`) |
| Spring Boot | 3.3.5 |
| JUnit 5 | (из Spring Boot) |
| RestAssured | 5.5.0 |
| AssertJ | (из Spring Boot) |
| Allure | 2.29.0 |

## Запуск тестируемого приложения

### Вариант A: локально из клона (рекомендуется на Windows)

Если рядом есть клон `spring-petclinic-rest`:

```bash
cd ../spring-petclinic-rest
./mvnw spring-boot:run
```

### Вариант B: Docker (как в задании)

```bash
docker run --rm -p 9966:9966 springcommunity/spring-petclinic-rest
```

### Вариант C: docker-compose (из этого репозитория)

```bash
docker compose up -d
```

> На Windows Docker Desktop проброс порта `9966` иногда «отваливается» (VPN / много контейнеров).
> Если `curl http://127.0.0.1:9966/petclinic/actuator/health` не отвечает — перезапусти контейнер или используй вариант A.

Проверки после запуска:

- API base: http://127.0.0.1:9966/petclinic
- Health: http://127.0.0.1:9966/petclinic/actuator/health
- Swagger: http://127.0.0.1:9966/petclinic/swagger-ui.html

## Запуск тестов

Приложение PetClinic должно быть запущено локально.

Maven Wrapper (если глобального `mvn` нет) — Windows PowerShell:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean test "-DbaseUrl=http://127.0.0.1:9966/petclinic"
```

Или глобальный Maven:

```bash
mvn clean test
mvn clean test -DbaseUrl=http://127.0.0.1:9966/petclinic
```

> На Windows предпочтительно `127.0.0.1`, а не `localhost` (IPv6-таймауты).

Отчёт Allure (опционально):

```powershell
.\mvnw.cmd allure:serve
```

## Соответствие ТЗ

| Требование | Статус |
|------------|--------|
| Отдельный тестовый проект (не внутри PetClinic) | да |
| Java 17+, Spring Boot, JUnit 5, Maven, RestAssured, AssertJ | да |
| Health: 200 + `status=UP` | да |
| Owner CRUD: POST→GET→PUT→DELETE→GET | да |
| Негативный create: status + тело ошибки | да |
| README: запуск app / тестов / версии | да |
| `-DbaseUrl=...` | да |
| Плюс: Allure, request/response logging, docker-compose | да |

## Структура

```
src/test/java/com/example/petclinic/
  config/ApiTestBase.java      # RestAssured + baseUrl (≈ conftest.py)
  client/HealthClient.java
  client/OwnersClient.java
  dto/                         # records ≈ Pydantic models
  tests/
```

В коде есть комментарии-шпаргалки Python → Java.

## Заметки по API PetClinic (SUT)

По коду `OwnerRestControllerV1`:

- `POST /api/owners` → **201**
- `PUT /api/owners/{id}` → **204** (данные проверяем следующим GET)
- `DELETE /api/owners/{id}` → **204**
- После удаления `GET` → **404**
- Ошибка валидации → **400** ProblemDetail (`title`, `detail`, `schemaValidationErrors`)
- Security в образе по умолчанию выключена — отдельный логин для тестов не нужен

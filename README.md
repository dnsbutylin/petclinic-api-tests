# PetClinic API Tests (Java)

Отдельный проект API-автотестов для [Spring PetClinic REST](https://github.com/spring-petclinic/spring-petclinic-rest).  
Структура близка к Python-референсу [`back_autotest_pydantic`](https://github.com/dnsbutylin/back_autotest_pydantic): clients / dto / tests + Allure.

## Стек

| Что | Версия / инструмент |
|-----|---------------------|
| Java | 17+ (проверено на 22) |
| Maven | 3.9+ |
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

> На Windows Docker Desktop проброс порта `9966` иногда «отваливается», если параллельно крутится много других контейнеров.
> Если `curl http://127.0.0.1:9966/petclinic/actuator/health` не отвечает — перезапусти контейнер или используй вариант A.

Проверки после запуска:

- API base: http://127.0.0.1:9966/petclinic
- Health: http://127.0.0.1:9966/petclinic/actuator/health
- Swagger: http://127.0.0.1:9966/petclinic/swagger-ui.html

## Запуск тестов

Приложение PetClinic должно быть запущено локально.

```bash
mvn clean test
```

С переопределением base URL:

```bash
mvn clean test -DbaseUrl=http://127.0.0.1:9966/petclinic
```

> На Windows предпочтительно `127.0.0.1`, а не `localhost` (иначе возможны таймауты из‑за IPv6).


Отчёт Allure (опционально):

```bash
mvn allure:serve
```

## Что покрыто

1. **Health check** — `GET /actuator/health` → 200, `status=UP`
2. **Owner CRUD** — create → get → update → delete → get(404)
3. **Негативный create** — пустые обязательные поля → 400 + тело ошибки (`ProblemDetail` / `schemaValidationErrors`)

## Структура

```
src/test/java/com/example/petclinic/
  config/ApiTestBase.java      # RestAssured + baseUrl (аналог conftest.py)
  client/HealthClient.java
  client/OwnersClient.java
  dto/                         # records ≈ Pydantic models
  tests/
```

## Заметки по API PetClinic

- `POST /api/owners` → **201**
- `PUT /api/owners/{id}` → **204** (проверка данных через последующий GET)
- `DELETE /api/owners/{id}` → **204**
- После удаления `GET` → **404**
- Ошибка валидации → **400** с `title`, `detail`, `schemaValidationErrors`

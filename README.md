# Bank Service Spring

REST API банковского сервиса на Spring Boot.

## Технологии

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Swagger/OpenAPI
* Docker
* Docker Compose
* JUnit 5
* Mockito

## Возможности

* Создание банковских аккаунтов
* Получение информации об аккаунте
* Перевод средств между аккаунтами
* История транзакций
* Пагинация транзакций
* Валидация входных данных
* Глобальная обработка ошибок

## Запуск через Docker

```bash
mvn test
mvn clean package
docker compose up --build
```

После запуска:

* API: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui.html

## Запуск локально

Создать файл `.env`:

```env
DB_URL=jdbc:postgresql://localhost:5432/bank
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

Запустить приложение:

```bash
mvn spring-boot:run
```

## Тесты

```bash
mvn test
```

## Архитектура

* Controller
* Service
* Repository
* DTO
* Exception Handler

## Реализовано

* DTO Mapping
* Pagination
* Validation
* Global Exception Handler
* Unit Tests
* Integration Tests
* Docker Support
* Pessimistic Locking

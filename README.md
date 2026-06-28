# Bank Service

[![Java CI](https://github.com/beksmaster/bank-service-spring/actions/workflows/maven.yml/badge.svg)](https://github.com/beksmaster/bank-service-spring/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=beksmaster_bank-service-spring&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=beksmaster_bank-service-spring)

Учебный REST API банковского сервиса на Spring Boot. Приложение позволяет регистрировать пользователей, создавать счета, переводить деньги и просматривать историю транзакций.

Проект демонстрирует не только CRUD-операции, но и работу с JWT-аутентификацией, транзакциями, конкурентным доступом к счетам, миграциями базы данных и интеграционными тестами в Testcontainers.

## Возможности

- регистрация и аутентификация пользователей;
- авторизация запросов через JWT;
- создание банковских счетов;
- доступ пользователя только к собственным счетам;
- переводы между счетами;
- проверка баланса и входных данных;
- запрет перевода на тот же счёт;
- пессимистическая блокировка счетов во время перевода;
- единый порядок блокировки для снижения риска дедлоков;
- сохранение и постраничный просмотр истории транзакций;
- документирование API через Swagger/OpenAPI;
- централизованная обработка ошибок;
- миграции PostgreSQL через Flyway;
- unit- и integration-тесты;
- запуск PostgreSQL в Testcontainers;
- CI и статический анализ в GitHub Actions.

## Технологии

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- PostgreSQL
- Flyway
- Maven
- JUnit 5 / Mockito
- Testcontainers
- Docker / Docker Compose
- Swagger / OpenAPI
- GitHub Actions
- SonarQube Cloud

## Архитектура

Приложение разделено на стандартные слои:

```text
HTTP request
    │
    ▼
Controller ──► DTO / Validation
    │
    ▼
Service ─────► Business logic / Transactions / Security
    │
    ▼
Repository ──► Spring Data JPA
    │
    ▼
PostgreSQL
```

Основные пакеты:

```text
src/main/java/com/example/bank
├── config       конфигурация приложения и OpenAPI
├── controller   REST-контроллеры
├── dto          модели запросов и ответов
├── exception    исключения и глобальный обработчик ошибок
├── model        JPA-сущности
├── repository   доступ к данным
├── security     конфигурация Spring Security и JWT-фильтр
└── service      бизнес-логика
```

## Требования

Для локального запуска необходимы:

- JDK 21;
- Maven 3.9+ или Maven Wrapper;
- Docker Desktop;
- Docker Compose.

Проверить окружение:

```bash
java -version
mvn -version
docker version
```

И Java, используемая Maven, должна иметь версию 21.

## Быстрый запуск через Docker Compose

Сначала соберите приложение. Во время тестов Testcontainers автоматически запустит отдельный PostgreSQL-контейнер, поэтому Docker должен быть доступен.

```bash
mvn clean package
docker compose up --build
```

После запуска:

- API: <http://localhost:8080>
- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

Остановить сервисы:

```bash
docker compose down
```

Удалить сервисы вместе с данными PostgreSQL:

```bash
docker compose down -v
```

## Локальный запуск

### 1. Запустить PostgreSQL

Можно использовать PostgreSQL из `docker-compose.yml`:

```bash
docker compose up -d postgres
```

### 2. Настроить окружение

Создайте в корне проекта файл `.env`:

```env
DB_URL=jdbc:postgresql://localhost:5432/bank
DB_USERNAME=postgres
DB_PASSWORD=Bank2026
```

Не добавляйте настоящий `.env` и секреты в Git.

### 3. Запустить приложение

```bash
mvn spring-boot:run
```

## Использование API

Все защищённые запросы должны содержать заголовок:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Регистрация

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "strong-password"
  }'
```

Пример ответа:

```json
{
  "id": 1,
  "username": "alice",
  "role": "USER"
}
```

### Вход

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "strong-password"
  }'
```

Пример ответа:

```json
{
  "token": "<JWT_TOKEN>"
}
```

### Создание счёта

```bash
curl -X POST http://localhost:8080/api/v1/accounts/create \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "balance": 1000.00
  }'
```

Пример ответа:

```json
{
  "accountNumber": "ACC-A1B2C3D4",
  "balance": 1000.00
}
```

### Получение счёта

```bash
curl http://localhost:8080/api/v1/accounts/ACC-A1B2C3D4 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Перевод денег

Списывать деньги можно только со счёта, принадлежащего текущему пользователю.

```bash
curl -X POST http://localhost:8080/api/v1/transfers/transfer \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccount": "ACC-A1B2C3D4",
    "toAccount": "ACC-E5F6G7H8",
    "amount": 150.00
  }'
```

При успешном переводе сервер возвращает HTTP `200 OK`.

### Получение транзакции

```bash
curl http://localhost:8080/api/v1/transactions/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### История транзакций счёта

```bash
curl "http://localhost:8080/api/v1/transactions/account/ACC-A1B2C3D4?page=0&size=20&sort=createdAt,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Основные эндпоинты

| Метод | Путь | Описание | Доступ |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Регистрация | Открытый |
| `POST` | `/api/v1/auth/login` | Получение JWT | Открытый |
| `POST` | `/api/v1/accounts/create` | Создание счёта | JWT |
| `GET` | `/api/v1/accounts/{number}` | Получение своего счёта | JWT, роль USER |
| `POST` | `/api/v1/transfers/transfer` | Перевод денег | JWT |
| `GET` | `/api/v1/transactions/{id}` | Получение транзакции | JWT |
| `GET` | `/api/v1/transactions/account/{number}` | История транзакций | JWT |

Полная интерактивная документация доступна в Swagger UI после запуска приложения.

## Тестирование

Запустить все тесты:

```bash
mvn test
```

Интеграционные тесты:

- запускают PostgreSQL 16 через Testcontainers;
- применяют миграции Flyway;
- поднимают Spring Application Context;
- проверяют репозитории, переводы и безопасность API.

Docker должен быть запущен до выполнения тестов.

## Миграции базы данных

Миграции находятся в каталоге:

```text
src/main/resources/db/migration
```

При запуске Flyway последовательно создаёт:

1. таблицы счетов и транзакций;
2. ограничения целостности;
3. таблицу пользователей;
4. связь счёта с владельцем.

Схема проверяется Hibernate с помощью `ddl-auto=validate`.

## CI и анализ качества

GitHub Actions выполняет:

1. настройку JDK 21;
2. Maven-сборку;
3. unit- и integration-тесты;
4. анализ проекта в SonarQube Cloud.

Для отправки анализа в SonarQube Cloud в настройках GitHub-репозитория должен быть создан секрет:

```text
SONAR_TOKEN
```

В SonarQube Cloud необходимо отключить `Automatic Analysis`, поскольку проект использует CI-based analysis.

## Обработка конкурентных переводов

Перевод выполняется внутри одной транзакции. Оба участвующих счёта загружаются с пессимистической блокировкой.

Чтобы два встречных перевода не блокировали счета в разном порядке, номера счетов предварительно сортируются. Это уменьшает вероятность взаимной блокировки транзакций.

После проверок балансы изменяются и создаётся запись о завершённой транзакции. При исключении вся операция откатывается.

## Замечания по безопасности

Проект является учебным. Перед использованием в production необходимо как минимум:

- хранить JWT-секрет только в переменных окружения или Secret Manager;
- не задавать пароли базы данных непосредственно в `docker-compose.yml`;
- настроить ротацию и срок жизни токенов;
- добавить refresh-токены или механизм повторной аутентификации;
- настроить HTTPS;
- ограничить Swagger UI для production-профиля;
- добавить аудит критичных операций;
- запретить произвольный начальный баланс счёта.

## Полезные команды

```bash
# Полная проверка проекта
mvn clean verify

# Запуск одного тестового класса
mvn -Dtest=TransferServiceTest test

# Запуск приложения
mvn spring-boot:run

# Сборка JAR без повторного запуска тестов
mvn package -DskipTests

# Просмотр контейнеров
docker compose ps

# Просмотр логов приложения
docker compose logs -f app
```

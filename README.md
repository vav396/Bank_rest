# 🚀 Система Управления Банковскими Картами

Backend-приложение на Java (Spring Boot) для управления банковскими картами с
поддержкой переводов, шифрования данных и ролевого доступа.

---

## 📋 Оглавление

- [Технологии](#-технологии)
- [Быстрый старт](#-быстрый-старт)
- [API Endpoints](#-api-endpoints)
- [Тесты](#-тесты)
- [Безопасность](#-безопасность)

---

## 🛠 Технологии

| Категория | Технологии |
|-----------|------------|
| **Язык** | Java 21 |
| **Фреймворк** | Spring Boot 3.2.0 |
| **Безопасность** | Spring Security + JWT |
| **БД** | PostgreSQL 15 |
| **ORM** | Spring Data JPA + Hibernate |
| **Миграции** | Liquibase |
| **Документация** | Swagger UI (OpenAPI 3.0) |
| **Контейнеризация** | Docker + Docker Compose |
| **Тесты** | JUnit 5 + Mockito |

---

## 🚀 Быстрый старт

### Вариант 1: Локальный запуск

1. Запусти PostgreSQL (Docker): docker-compose up -d bankcards-db
2. Собери проект: ./mvnw clean install
3. Запусти приложение:./mvnw spring-boot:run
4. Открой Swagger UI: http://localhost:8080/swagger-ui.htm


### Вариант 2: Docker Compose (полная среда)

- Запуск всех сервисов: docker-compose up -d
- Остановка: docker-compose down
- Просмотр логов: docker-compose logs -f
---

## 📡 API Endpoints

### Auth Controller
| Метод | Endpoint | Описание |
|-------|----------|----------|
| POST | /api/auth/register | Регистрация пользователя |
| POST | /api/auth/login | Аутентификация (получение JWT) |

### Card Controller
| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | /api/cards | Получить все карты (пагинация) |
| POST | /api/cards | Создать новую карту |
| GET | /api/cards/{id} | Получить карту по ID |
| GET | /api/cards/{id}/masked | Получить маскированный номер |
| GET | /api/cards/user/{userId} | Карты пользователя |

### Transfer Controller
| Метод | Endpoint | Описание |
|-------|----------|----------|
| POST | /api/transfers | Перевод между картами |

Полная документация: http://localhost:8080/swagger-ui.html

---

## ✅ Тесты

Покрытие юнит-тестами ключевой бизнес-логики:

- Запустить все тесты: ./mvnw test
- CardService тесты: ./mvnw test -Dtest=CardServiceTest
- TransferService тесты: ./mvnw test -Dtest=TransferServiceTest
  Покрытые сценарии:
- Создание карты (шифрование номера)
- Получение карты (расшифровка)
- Перевод между картами (успех/ошибки)
- Проверка баланса и прав доступа

---

## 🔒 Безопасность

| Функция | Реализация |
|---------|------------|
| Аутентификация | JWT токены (access token) |
| Авторизация | Роли: ADMIN, USER |
| Шифрование | AES-256 для номеров карт |
| Маскирование | **** **** **** 1234 для отображения |
| Валидация | Jakarta Validation (@NotNull, @Pattern, etc.) |

---

## 📁 Структура проекта
src/
├── main/
│ ├── java/com.example.bankcards/
│ │ ├── config/ # Конфигурации Security, OpenAPI
│ │ ├── controller/ # REST контроллеры
│ │ ├── dto/ # Data Transfer Objects
│ │ ├── entity/ # JPA сущности
│ │ ├── exception/ # Обработчики ошибок
│ │ ├── repository/ # Репозитории
│ │ ├── security/ # JWT фильтр, UserDetails
│ │ ├── service/ # Бизнес-логика + тесты
│ │ └── util/ # Утилиты (шифрование, маскирование)
│ └── resources/
│ └── db/migration/ # Liquibase миграции
└── test/
└── java/.../service/ # Юнит-тесты
---

## 📊 Статус проекта

| Модуль | Статус |
|--------|--------|
| Модуль 1-5 | ✅ Завершены |
| Модуль 6 (Тесты, Swagger, README, Docker) | ✅ Завершён |

---

## 👨‍💻 Автор

Александр — Java-разработчик
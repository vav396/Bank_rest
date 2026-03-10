# 🚀 Система Управления Банковскими Картами

Backend-приложение на Java (Spring Boot) для управления банковскими картами с
поддержкой переводов, шифрования данных и ролевого доступа.

---

## 📋 Оглавление

- [Технологии](#-технологии)
- [Быстрый старт](#-быстрый-старт)
- [Роли и права](#-роли-и-права)
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
2. Собери проект: mvn clean install
3. Запусти приложение:.mvn spring-boot:run
4. Открой Swagger UI: http://localhost:8080/swagger-ui.html


### Вариант 2: Docker Compose (полная среда)

- Запуск всех сервисов: docker-compose up -d
- Остановка: docker-compose down
- Просмотр логов: docker-compose logs -f
- 
---

## 👥 Роли и права
### 🔐 Роли

- USER - Обычный пользователь: управляет только своими картами
- ADMIN - Администратор: полный доступ ко всем функциям

 ### 🔑 Права доступа

 ### ✅ Пользователь (USER)

- Просмотр своих карт: GET /api/cards/me
- Баланс карты: GET /api/cards/me/{id}/balance
- Запрос на блокировку: POST /api/cards/me/{id}/block-request
- Переводы между своими картами: POST /api/transfers
- Создание новой карты: POST /api/cards

### ✅ Администратор (ADMIN)

- Всё, что может USER +
- Блокировка/активация любой карты: PUT /api/cards/{id}/block|activate
- Удаление любой карты: DELETE /api/cards/{id}
- Просмотр всех карт: GET /api/cards
- Управление пользователями: POST/PUT/DELETE /api/admin/users
- Просмотр всех пользователей: GET /api/admin/users


### 📡 API Endpoints

### 🔐 Auth Controller

- POST /api/auth/register — Регистрация пользователя
- POST /api/auth/login — Аутентификация (получение JWT)
- 
### 💳 Card Controller (общие)

- GET /api/cards — Все карты (пагинация) [ADMIN]
- POST /api/cards — Создать карту [Авторизован]
- GET /api/cards/{id} — Карта по ID [Авторизован]
- GET /api/cards/{id}/masked — Маскированный номер [Авторизован]
- GET /api/cards/user/{userId} — Карты пользователя по ID [Авторизован]
- DELETE /api/cards/{id} — Удалить карту [ADMIN]
- 
### 👤 My Cards (пользователь)

- GET /api/cards/me — Мои карты (пагинация) [USER]
- GET /api/cards/me/{id}/balance — Баланс карты [USER]
- POST /api/cards/me/{id}/block-request — Запрос блокировки [USER]
- 
### 🔧 Admin Cards

- PUT /api/cards/{id}/block — Заблокировать карту [ADMIN]
- PUT /api/cards/{id}/activate — Активировать карту [ADMIN]
- 
### 👥 Admin Users

- GET /api/admin/users — Все пользователи с картами [ADMIN]
- POST /api/admin/users — Создать пользователя [ADMIN]
- PUT /api/admin/users/{id} — Обновить пользователя [ADMIN]
- DELETE /api/admin/users/{id} — Удалить пользователя [ADMIN]
- 
### 💸 Transfers

- POST /api/transfers — Перевод между картами [USER]
- 
### 📚 Полная документация: http://localhost:8080/swagger-ui.html

---

## ✅ Тесты

Покрытие юнит-тестами ключевой бизнес-логики:

- Запустить все тесты: mvn test
- CardService тесты: mvn test -Dtest=CardServiceTest
- TransferService тесты: mvn test -Dtest=TransferServiceTest
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

## 👨‍💻 Автор

Александр — Java-разработчик
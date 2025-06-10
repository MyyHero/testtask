Это REST-сервис на Spring Boot 3 с хранением данных в PostgreSQL и кешем в Redis, аутентификация через JWT.
Для запуска подойдёт любой из вариантов:

Локально

Запустить PostgreSQL на localhost:15432 (БД user_db, user/postgres) и Redis на localhost:6379.

mvn clean spring-boot:run

Через Docker Compose


docker-compose up --build
(БД и Redis сразу поднимаются, приложение — на порту 8080)

Swagger UI доступен по адресу


http://localhost:8080/swagger-ui.html
Нажмите Authorize и вставьте JWT из /api/v1/auth/login.

Тесты

Unit-тесты для логики переводов

Интеграционный тест (MockMvc + Testcontainers)

Особенности

Баланс автоматически растёт на 10% каждые 30 секунд (до 207% от исходного депозита)

Переводы защищены пессимистичными блокировками

Кэширование через Redis

Дефолтные учётки (Flyway-миграции при старте):

User 1:

email alice@ex.com

phone 75550000006

пароль pwd1

User 2:

email bob@ex.com

phone 75550000086

пароль pwd2

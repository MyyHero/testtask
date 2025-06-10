Test-Task API: Spring Boot 3 + Postgres + Redis + JWT.
Запуск: mvn clean spring-boot:run, БД — jdbc:postgresql://localhost:15432/user_db, Redis — localhost:6379.
Swagger: http://localhost:8080/swagger-ui.html, авторизуйтесь через кнопку Authorize (вставьте JWT).
Есть юнит-тесты перевода средств и один интеграционный сценарий (MockMvc + Testcontainers).
Кэширование — Redis с Jackson JavaTimeModule.
Баланс индексируется каждые 30 секунд, переводы защищены pessimistic-lock.
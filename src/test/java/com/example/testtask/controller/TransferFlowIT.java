package com.example.testtask.controller;

import com.example.testtask.entity.Account;
import com.example.testtask.entity.EmailData;
import com.example.testtask.entity.User;
import com.example.testtask.repository.AccountRepository;
import com.example.testtask.repository.EmailDataRepository;
import com.example.testtask.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TransferFlowIT {

    @Container
    static final PostgreSQLContainer<?> db =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("user_db")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      db::getJdbcUrl);
        r.add("spring.datasource.username", db::getUsername);
        r.add("spring.datasource.password", db::getPassword);
    }

    @Container
    static final GenericContainer<?> redis =
            new GenericContainer<>("redis:7-alpine")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        // DataSource (Postgres)
        r.add("spring.datasource.url",      db::getJdbcUrl);
        r.add("spring.datasource.username", db::getUsername);
        r.add("spring.datasource.password", db::getPassword);

        // Redis
        r.add("spring.data.redis.host", redis::getHost);
        r.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired UserRepository      userRepo;
    @Autowired EmailDataRepository emailRepo;
    @Autowired AccountRepository   accRepo;
    @Autowired PasswordEncoder     encoder;
    @Autowired MockMvc             mvc;
    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void seed() {
        // 1) Полностью сбрасываем таблицы
        accRepo.deleteAll();
        emailRepo.deleteAll();
        userRepo.deleteAll();

        // 2) Сбрасываем sequences через JdbcTemplate (auto-commit)
        jdbc.execute("ALTER SEQUENCE user_seq RESTART WITH 1");
        jdbc.execute("ALTER SEQUENCE account_seq RESTART WITH 1");
        jdbc.execute("ALTER SEQUENCE email_seq RESTART WITH 1");
        jdbc.execute("ALTER SEQUENCE phone_seq RESTART WITH 1");

        // 3) Засеиваем Alice(id=1)/Bob(id=2)
        User alice = userRepo.save(User.builder()
                .name("Alice")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .password(encoder.encode("pwd1"))
                .build());

        User bob = userRepo.save(User.builder()
                .name("Bob")
                .dateOfBirth(LocalDate.of(1995, 2, 2))
                .password(encoder.encode("pwd2"))
                .build());

        emailRepo.save(new EmailData(null, alice, "alice@ex.com"));
        emailRepo.save(new EmailData(null, bob,   "bob@ex.com"));

        accRepo.save(new Account(null, alice, new BigDecimal("1000"), BigDecimal.ZERO));
        accRepo.save(new Account(null, bob,   new BigDecimal("50"),   BigDecimal.ZERO));
    }



    private String loginByEmail(String email, String pass) throws Exception {
        String json = mvc.perform(post("/api/v1/auth/login/email")
                        .with(csrf())                              //  ← вот эта строка
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"email":"%s","password":"%s"}""".formatted(email, pass)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return "Bearer " + JsonPath.read(json, "$.token");
    }

    @Test
    void transfer_ok_updatesBothBalances() throws Exception {
        String tokenA = loginByEmail("alice@ex.com", "pwd1");

        mvc.perform(post("/api/v1/account/transfer")
                        .header("Authorization", tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                 {"targetUserId": 2, "amount": 100}"""))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/users/1").header("Authorization", tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("900.00"));

        mvc.perform(get("/api/v1/users/2").header("Authorization", tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("150.00"));
    }
}

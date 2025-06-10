package com.example.testtask.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Пользователь системы")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Column(length = 500, nullable = false)
    @Schema(description = "Имя пользователя", example = "Василий Иванов")
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    @Schema(description = "Дата рождения", example = "1993-05-01", format = "date")
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 500)
    @Schema(description = "Пароль (хэш)", example = "$2a$10$...")
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "Связанный аккаунт пользователя")
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список телефонов")
    private List<PhoneData> phones = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список email-адресов")
    private List<EmailData> emails = new ArrayList<>();
}

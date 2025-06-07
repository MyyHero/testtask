package com.example.testtask.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Аккаунт пользователя с балансом")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    @Schema(description = "ID аккаунта", example = "10")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "Пользователь, которому принадлежит аккаунт")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Текущий баланс", example = "1500.75")
    private BigDecimal balance;

    @Column(name = "initial_deposit", nullable = false)
    @Schema(description = "Начальный депозит (для лимита в 207%)", example = "1000.00")
    private BigDecimal initialDeposit;
}

package com.example.testtask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация об аккаунте")
public class AccountResponse {

    @Schema(description = "ID аккаунта", example = "1")
    private Long id;

    @Schema(description = "Баланс аккаунта", example = "1500.00")
    private BigDecimal balance;

    @Schema(description = "Начальный депозит", example = "1000.00")
    private BigDecimal initialDeposit;
}

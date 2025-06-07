package com.example.testtask.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на перевод денег между аккаунтами")
public class TransferRequest {

    @NotNull
    @Schema(description = "ID пользователя, кому переводим", example = "2")
    private Long targetUserId;

    @NotNull
    @Positive
    @Schema(description = "Сумма перевода", example = "500.00")
    private BigDecimal amount;
}

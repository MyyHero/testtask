package com.example.testtask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneResponse {
    @Schema(description = "ID записи телефона", example = "1")
    private Long id;

    @Schema(description = "Номер телефона", example = "+79001234567")
    private String phone;
}

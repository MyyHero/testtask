package com.example.testtask.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Запрос аутентификации по телефону")
public class PhoneAuthRequest {
    @Schema(description = "Телефон пользователя", example = "79207865432")
    @NotBlank
    @Pattern(regexp = "79\\d{9}", message = "Неверный формат телефона")
    private String phone;

    @Schema(description = "Пароль", example = "password123")
    @NotBlank
    private String password;
}

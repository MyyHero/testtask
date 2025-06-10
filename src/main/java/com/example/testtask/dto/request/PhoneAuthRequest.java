package com.example.testtask.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "Запрос аутентификации по телефону")
public class PhoneAuthRequest {
    @Schema(description = "Телефон пользователя", example = "79207865432")
    @NotBlank
    @Pattern(regexp = "7\\d{10}", message = "Неверный формат телефона")
    private String phone;

    @Schema(description = "Пароль", example = "password123")
    @NotBlank
    private String password;
}

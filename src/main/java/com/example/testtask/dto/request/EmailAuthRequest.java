package com.example.testtask.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Запрос аутентификации по email")
public class EmailAuthRequest {

    @Schema(description = "Email пользователя", example = "user@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Пароль", example = "password123")
    @NotBlank
    private String password;
}

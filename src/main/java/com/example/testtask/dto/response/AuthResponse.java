package com.example.testtask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ при успешной аутентификации")
public class AuthResponse {

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}

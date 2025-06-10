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
@Schema(description = "Ответ при успешной аутентификации")
public class AuthResponse {

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}

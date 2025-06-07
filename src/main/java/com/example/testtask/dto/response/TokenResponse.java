package com.example.testtask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с JWT токеном")
public class TokenResponse {

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
}

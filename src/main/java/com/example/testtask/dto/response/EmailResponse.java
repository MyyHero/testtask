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
@Schema(description = "Ответ с email-адресом")
public class EmailResponse {

    @Schema(description = "ID email-записи", example = "1")
    private Long id;

    @Schema(description = "Email", example = "user@example.com")
    private String email;
}

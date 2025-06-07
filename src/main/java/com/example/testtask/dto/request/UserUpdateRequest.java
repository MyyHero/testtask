package com.example.testtask.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на обновление данных пользователя")
public class UserUpdateRequest {

    @NotBlank
    @Size(max = 500)
    @Schema(description = "Имя пользователя", example = "Василий Иванов")
    private String name;

    @NotNull
    @Schema(description = "Дата рождения", example = "1993-05-01")
    private String dateOfBirth; // можно оставить строкой и преобразовывать

    @NotBlank
    @Size(min = 8, max = 500)
    @Schema(description = "Пароль", example = "securePassword123")
    private String password;

    @NotEmpty
    @Schema(description = "Список email-ов пользователя")
    private List<@Email @Size(max = 200) String> emails;

    @NotEmpty
    @Schema(description = "Список телефонов пользователя (формат: 79207865432)")
    private List<@Pattern(regexp = "\\d{11,13}") String> phones;
}

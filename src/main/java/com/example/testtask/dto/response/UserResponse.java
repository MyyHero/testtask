package com.example.testtask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Информация о пользователе")
public class UserResponse {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Василий Иванов")
    private String name;

    @Schema(description = "Дата рождения", example = "1993-05-01")
    private LocalDate dateOfBirth;

    @Schema(description = "Список email-ов")
    private List<String> emails;

    @Schema(description = "Список телефонов")
    private List<String> phones;

    @Schema(description = "Текущий баланс")
    private String balance;
}

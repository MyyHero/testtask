package com.example.testtask.controller;

import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @Operation(summary = "Поиск пользователей по фильтрам")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @Parameter(description = "Фильтр по имени (начинается с)") @RequestParam(required = false) String name,
            @Parameter(description = "Фильтр по email (точное совпадение)") @RequestParam(required = false) String email,
            @Parameter(description = "Фильтр по телефону (точное совпадение)") @RequestParam(required = false) String phone,
            @Parameter(description = "Фильтр по дате рождения после") @RequestParam(required = false) String dateOfBirthAfter,
            @PageableDefault(size = 10, page = 0) @ParameterObject Pageable pageable
    ) {
        List<UserResponse> result = userService.searchUsers(
                name, email, phone, dateOfBirthAfter,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        return ResponseEntity.ok(result);
    }


}

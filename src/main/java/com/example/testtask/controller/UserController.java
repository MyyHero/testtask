package com.example.testtask.controller;

import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Поиск пользователей")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String dateOfBirthAfter,
            @ParameterObject Pageable pageable) {

        List<UserResponse> list = userService.searchUsers(
                name, email, phone, dateOfBirthAfter,
                pageable
        );
        return ResponseEntity.ok(list);
    }


}

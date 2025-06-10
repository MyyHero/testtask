package com.example.testtask.controller.auth;

import com.example.testtask.dto.request.EmailAuthRequest;
import com.example.testtask.dto.request.PhoneAuthRequest;
import com.example.testtask.dto.response.AuthResponse;
import com.example.testtask.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PermitAll
@RestController
@RequestMapping("/api/v1/auth/login")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Аутентификация по email и по телефону")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/email")
    @Operation(summary = "Аутентификация по email")
    public ResponseEntity<AuthResponse> authenticateByEmail(@Valid @RequestBody EmailAuthRequest request) {
        String token = authService.authenticateByEmail(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/phone")
    @Operation(summary = "Аутентификация по номеру телефона")
    public ResponseEntity<AuthResponse> authenticateByPhone(@Valid @RequestBody PhoneAuthRequest request) {
        String token = authService.authenticateByPhone(request.getPhone(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

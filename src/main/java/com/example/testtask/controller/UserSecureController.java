package com.example.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserSecureController {

    @GetMapping("/me")
    public ResponseEntity<String> getMe(Authentication auth) {
        Long userId = (Long) auth.getPrincipal(); // из токена
        return ResponseEntity.ok("Ваш ID: " + userId);
    }
}
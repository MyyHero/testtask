package com.example.testtask.controller;

import com.example.testtask.dto.request.CreateEmailRequest;
import com.example.testtask.dto.request.CreatePhoneRequest;
import com.example.testtask.dto.request.UpdateEmailRequest;
import com.example.testtask.dto.request.UpdatePhoneRequest;
import com.example.testtask.dto.response.EmailResponse;
import com.example.testtask.dto.response.PhoneResponse;
import com.example.testtask.service.EmailService;
import com.example.testtask.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Управление собственными email и телефонами")
public class UserProfileController {

    private final EmailService emailService;
    private final PhoneService phoneService;


    @Operation(summary = "Добавить email")
    @PostMapping("/emails")
    public ResponseEntity<EmailResponse> addEmail(@Valid @RequestBody CreateEmailRequest request) {
        return ResponseEntity.ok(emailService.addEmail(request));
    }

    @Operation(summary = "Обновить email")
    @PutMapping("/emails/{emailId}")
    public ResponseEntity<EmailResponse> updateEmail(
            @PathVariable Long emailId,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        return ResponseEntity.ok(emailService.updateEmail(emailId, request));
    }

    @Operation(summary = "Удалить email")
    @DeleteMapping("/emails/{emailId}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long emailId) {
        emailService.deleteEmail(emailId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить все свои email'ы")
    @GetMapping("/emails")
    public ResponseEntity<List<EmailResponse>> getUserEmails() {
        return ResponseEntity.ok(emailService.getUserEmails());
    }


    @Operation(summary = "Добавить телефон")
    @PostMapping("/phones")
    public ResponseEntity<PhoneResponse> addPhone(@Valid @RequestBody CreatePhoneRequest request) {
        return ResponseEntity.ok(phoneService.addPhone(request));
    }

    @Operation(summary = "Обновить телефон")
    @PutMapping("/phones/{phoneId}")
    public ResponseEntity<PhoneResponse> updatePhone(
            @PathVariable Long phoneId,
            @Valid @RequestBody UpdatePhoneRequest request
    ) {
        return ResponseEntity.ok(phoneService.updatePhone(phoneId, request));
    }

    @Operation(summary = "Удалить телефон")
    @DeleteMapping("/phones/{phoneId}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long phoneId) {
        phoneService.deletePhone(phoneId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить все свои телефоны")
    @GetMapping("/phones")
    public ResponseEntity<List<PhoneResponse>> getUserPhones() {
        return ResponseEntity.ok(phoneService.getUserPhones());
    }
}

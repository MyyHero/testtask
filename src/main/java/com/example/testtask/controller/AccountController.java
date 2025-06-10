package com.example.testtask.controller;

import com.example.testtask.dto.request.TransferRequest;
import com.example.testtask.dto.response.AccountResponse;
import com.example.testtask.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Аккаунт", description = "Операции с аккаунтом и переводами")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Получить свой аккаунт и баланс")
    @GetMapping
    public ResponseEntity<AccountResponse> getMyAccount() {

        return ResponseEntity.ok(accountService.getCurrentUserAccount());
    }

    @Operation(summary = "Перевод денег другому пользователю")
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        accountService.transferToAnotherUser(request.getAmount(), request.getTargetUserId());
        return ResponseEntity.noContent().build();
    }
}

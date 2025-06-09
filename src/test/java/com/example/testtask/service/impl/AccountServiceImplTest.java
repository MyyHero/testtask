package com.example.testtask.service.impl;

import com.example.testtask.entity.Account;
import com.example.testtask.entity.User;
import com.example.testtask.exception.AccountNotFoundException;
import com.example.testtask.exception.InsufficientFundsException;
import com.example.testtask.mapper.AccountMapper;
import com.example.testtask.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transfer_shouldTransferMoneySuccessfully() {
        // Пользователи
        User fromUser = mock(User.class);
        when(fromUser.getId()).thenReturn(1L);

        User toUser = mock(User.class);
        when(toUser.getId()).thenReturn(2L);

        // Аккаунты
        Account fromAccount = new Account();
        fromAccount.setUser(fromUser);
        fromAccount.setBalance(BigDecimal.valueOf(500));
        fromAccount.setInitialDeposit(BigDecimal.valueOf(500));

        Account toAccount = new Account();
        toAccount.setUser(toUser);
        toAccount.setBalance(BigDecimal.valueOf(200));
        toAccount.setInitialDeposit(BigDecimal.valueOf(200));

        // Сначала от меньшего ID к большему — как в логике
        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        // Действие
        accountService.transfer(1L, 2L, BigDecimal.valueOf(100));

        // Проверка
        assertEquals(BigDecimal.valueOf(400), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(300), toAccount.getBalance());
    }

    @Test
    void transfer_shouldThrowExceptionWhenInsufficientFunds() {
        User fromUser = mock(User.class);
        when(fromUser.getId()).thenReturn(1L);

        User toUser = mock(User.class);
        when(toUser.getId()).thenReturn(2L);

        Account fromAccount = new Account();
        fromAccount.setUser(fromUser);
        fromAccount.setBalance(BigDecimal.valueOf(50));
        fromAccount.setInitialDeposit(BigDecimal.valueOf(50));

        Account toAccount = new Account();
        toAccount.setUser(toUser);
        toAccount.setBalance(BigDecimal.valueOf(200));
        toAccount.setInitialDeposit(BigDecimal.valueOf(200));

        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.valueOf(100));
        });
    }

    @Test
    void transfer_shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findByUserIdForUpdate(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.valueOf(100));
        });
    }
}

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
import java.util.HashMap;
import java.util.Map;
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

    private final User fromUser = new User();
    private final User toUser = new User();
    private final Account fromAccount = new Account();
    private final Account toAccount = new Account();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fromUser.setId(1L);
        toUser.setId(2L);

        fromAccount.setUser(fromUser);
        fromAccount.setBalance(new BigDecimal("500.00"));
        fromAccount.setInitialDeposit(new BigDecimal("500.00"));

        toAccount.setUser(toUser);
        toAccount.setBalance(new BigDecimal("200.00"));
        toAccount.setInitialDeposit(new BigDecimal("200.00"));
    }

    @Test
    void transfer_shouldTransferMoneySuccessfully() {
        Map<Long, Account> map = new HashMap<>();
        map.put(1L, fromAccount);
        map.put(2L, toAccount);

        when(accountRepository.findByUserIdForUpdate(anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);
                    return Optional.ofNullable(map.get(userId));
                });

        when(accountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        accountService.transfer(1L, 2L, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("400.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("300.00"), toAccount.getBalance());
    }

    @Test
    void transfer_shouldThrowExceptionWhenInsufficientFunds() {
        fromAccount.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer(1L, 2L, new BigDecimal("100.00")));
    }

    @Test
    void transfer_shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findByUserIdForUpdate(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer(1L, 2L, new BigDecimal("100.00")));
    }
}

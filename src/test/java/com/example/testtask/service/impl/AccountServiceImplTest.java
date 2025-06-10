package com.example.testtask.service.impl;

import com.example.testtask.entity.Account;
import com.example.testtask.entity.User;
import com.example.testtask.exception.*;
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

    @Mock  private AccountRepository accountRepository;
    @Mock  private AccountMapper     accountMapper;
    @InjectMocks
    private AccountServiceImpl       accountService;

    private final User    fromUser   = new User();
    private final User    toUser     = new User();
    private final Account fromAcc    = new Account();
    private final Account toAcc      = new Account();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fromUser.setId(1L);
        toUser.setId(2L);

        fromAcc.setUser(fromUser);
        fromAcc.setBalance(new BigDecimal("500.00"));
        fromAcc.setInitialDeposit(new BigDecimal("500.00"));

        toAcc.setUser(toUser);
        toAcc.setBalance(new BigDecimal("200.00"));
        toAcc.setInitialDeposit(new BigDecimal("200.00"));
    }

    @Test
    void transfer_movesMoney() {
        Map<Long,Account> map = new HashMap<>();
        map.put(1L, fromAcc);
        map.put(2L, toAcc);

        when(accountRepository.findByUserIdForUpdate(anyLong()))
                .thenAnswer(inv -> Optional.of(map.get(inv.getArgument(0))));
        when(accountRepository.saveAll(any())).thenReturn(null);

        accountService.transfer(1L, 2L, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("400.00"), fromAcc.getBalance());
        assertEquals(new BigDecimal("300.00"), toAcc.getBalance());
    }


    @Test
    void transfer_throwsWhenInsufficientFunds() {
        fromAcc.setBalance(new BigDecimal("50.00"));

        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAcc));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAcc));

        assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer(1L, 2L, new BigDecimal("100.00")));
    }

    @Test
    void transfer_throwsWhenAccountNotFound() {
        when(accountRepository.findByUserIdForUpdate(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer(1L, 2L, new BigDecimal("100.00")));
    }

    @Test
    void transfer_throwsOnSelfTransfer() {
        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAcc));

        assertThrows(SelfTransferException.class,
                () -> accountService.transfer(1L, 1L, new BigDecimal("50")));
    }

    @Test
    void transfer_throwsOnNegativeOrZeroAmount() {
        when(accountRepository.findByUserIdForUpdate(1L)).thenReturn(Optional.of(fromAcc));
        when(accountRepository.findByUserIdForUpdate(2L)).thenReturn(Optional.of(toAcc));

        assertAll(
                () -> assertThrows(InvalidTransferAmountException.class,
                        () -> accountService.transfer(1L, 2L, BigDecimal.ZERO)),
                () -> assertThrows(InvalidTransferAmountException.class,
                        () -> accountService.transfer(1L, 2L, new BigDecimal("-5")))
        );
    }
}

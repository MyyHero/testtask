package com.example.testtask.service;

import com.example.testtask.dto.response.AccountResponse;
import com.example.testtask.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);

    void processScheduledInterest();

    AccountResponse getCurrentUserAccount();

    void transferToAnotherUser(BigDecimal amount, Long targetUserId);


}

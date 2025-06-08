package com.example.testtask.service.impl;

import com.example.testtask.dto.response.AccountResponse;
import com.example.testtask.entity.Account;
import com.example.testtask.exception.AccountNotFoundException;
import com.example.testtask.exception.InvalidBalanceAmountException;
import com.example.testtask.exception.InvalidTransferAmountException;
import com.example.testtask.exception.SelfTransferException;
import com.example.testtask.mapper.AccountMapper;
import com.example.testtask.repository.AccountRepository;
import com.example.testtask.security.SecurityUtil;
import com.example.testtask.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    @Override
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) {
            log.warn("Попытка перевести средства самому себе: userId={}", fromUserId);
            throw new SelfTransferException("Нельзя делать перевод самому себе");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Отрицательная или нулевая сумма перевода: {} руб. от пользователя {}", amount, fromUserId);
            throw new InvalidTransferAmountException("Сумма перевода должна быть положительной");
        }
        Long firstId = Math.min(fromUserId, toUserId);
        Long secondId = Math.max(fromUserId, toUserId);

        Account first = accountRepository.findByUserIdForUpdate(firstId).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден: " + firstId));
        Account second = accountRepository.findByUserIdForUpdate(secondId).orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден: " + secondId));

        Account from = fromUserId.equals(first.getUser().getId()) ? first : second;
        Account to = toUserId.equals(first.getUser().getId()) ? second : first;
        if (from.getBalance().compareTo(amount) < 0) {
            log.warn("Недостаточно средств у пользователя {} для перевода {} руб.", fromUserId, amount);

            throw new InvalidBalanceAmountException("Недостаточно средств");
        }
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        log.info("Перевод {} руб. от пользователя {} к {} выполнен успешно", amount, fromUserId, toUserId);


    }

    @Scheduled(fixedRate = 30_000)
    @Transactional
    @Override
    public void processScheduledInterest() {
        log.info("Запуск начисления процентов по аккаунтам");
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal current = account.getBalance();
            BigDecimal limit = account.getInitialDeposit().multiply(BigDecimal.valueOf(2.07));

            if (current.compareTo(limit) < 0) {
                BigDecimal increased = current.multiply(BigDecimal.valueOf(1.1));
                BigDecimal newBalance = increased.min(limit);
                account.setBalance(newBalance);
                log.info("Проценты начислены: аккаунт ID={}, старый баланс={}, новый баланс={}",
                        account.getId(), current, newBalance);
            }
        }
    }


    @Override
    public AccountResponse getCurrentUserAccount() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Account account = accountRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new AccountNotFoundException("Аккаунт не найден: " + currentUserId));
        return accountMapper.toDto(account);
    }

    @Transactional
    @Override
    public void transferToAnotherUser(BigDecimal amount, Long targetUserId) {
        Long fromUserId = SecurityUtil.getCurrentUserId();
        transfer(fromUserId, targetUserId, amount);
    }




}



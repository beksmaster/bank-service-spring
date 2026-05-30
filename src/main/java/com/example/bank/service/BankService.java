package com.example.bank.service;

import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankService {

    private final AccountRepository accountRepository;

    public BankService(AccountRepository repo) {
        this.accountRepository = repo;
    }

    @Transactional
    public void transfer(String from, String to, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }
        Account fromAcc = accountRepository.findByNumberForUpdate(from)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт отправителя не найден"));

        Account toAcc = accountRepository.findByNumberForUpdate(to)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт получателя не найден"));


        if (fromAcc.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств");
        }
        fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
        toAcc.setBalance(toAcc.getBalance().add(amount));

    }
}
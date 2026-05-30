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

        String first;
        String second;
        if(from.compareTo(to)< 0){
            first = from;
            second = to;
        }else{
            first = to;
            second = from;
        }

        Account acc1 = accountRepository.findByNumberForUpdate(first) // блокируем через FOR UPDATE
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт отправителя не найден"));
        Account acc2 = accountRepository.findByNumberForUpdate(second)
                .orElseThrow(() -> new IllegalArgumentException("Аккаунт получателя не найден"));

        Account fromAcc;
        Account toAcc;

        if(from.equals(first)){ // если аккаунты совпадают, то оставляем как есть.
            fromAcc = acc1;
            toAcc = acc2;
        }else{
            fromAcc = acc2; // если не совпадают то меняем между собой
            toAcc = acc1;
        }


        if (fromAcc.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств");
        }

        fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
        toAcc.setBalance(toAcc.getBalance().add(amount));

    }
}
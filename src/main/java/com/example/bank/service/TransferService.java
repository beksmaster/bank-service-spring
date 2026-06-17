package com.example.bank.service;

import com.example.bank.enums.TransactionStatus;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository repo, TransactionRepository transactionRepository) {
        this.accountRepository = repo;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(String from, String to, BigDecimal amount) {

        if (from.equals(to)) {
            throw new IllegalArgumentException("Self transfer not allowed");
        }

        validateAmount(amount);

        String first = from.compareTo(to) < 0 ? from : to;
        String second = from.compareTo(to) < 0 ? to : from;

        Account firstAcc = accountRepository.findByNumberForUpdate(first)
                .orElseThrow(AccountNotFoundException::new);

        Account secondAcc = accountRepository.findByNumberForUpdate(second)
                .orElseThrow(AccountNotFoundException::new);

        Account fromAcc = from.equals(first) ? firstAcc : secondAcc;
        Account toAcc = from.equals(first) ? secondAcc : firstAcc;

        validateFunds(fromAcc, amount);

        log.info(
                "Transfer started: {} -> {} amount={}",
                from,
                to,
                amount
        );
        applyTransfer(fromAcc, toAcc, amount);
        log.info(
                "Transfer completed: {} -> {} amount={}",
                from,
                to,
                amount
        );

        Transaction transaction = new Transaction(fromAcc, toAcc, amount, TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
    }

    private void validateAmount(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InsufficientFundsException();
        }
    }

    private void validateFunds(Account fromAcc, BigDecimal amount){
        if (fromAcc.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }
    private void applyTransfer(Account from, Account to, BigDecimal amount){
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }
}
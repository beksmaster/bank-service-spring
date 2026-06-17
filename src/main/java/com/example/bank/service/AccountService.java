package com.example.bank.service;

import com.example.bank.dto.AccountRequest;
import com.example.bank.dto.AccountResponse;
import com.example.bank.exception.AccountAlreadyExistsException;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String number){
        Account account = accountRepository.findById(number)
                        .orElseThrow(AccountNotFoundException::new);

        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance()
        );
    }


    public AccountResponse createAccount(AccountRequest request) {
        if(accountRepository.existsById(request.accountNumber())){
            throw new AccountAlreadyExistsException();
        }
        if(request.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        Account account = new Account(
                request.accountNumber(),
                request.balance());
        accountRepository.save(account);
        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance());
    }
}

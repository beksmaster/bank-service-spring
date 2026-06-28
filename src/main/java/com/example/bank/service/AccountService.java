package com.example.bank.service;

import com.example.bank.dto.AccountRequest;
import com.example.bank.dto.AccountResponse;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.UserNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;



@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CurrentUserService currentUser;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, CurrentUserService currentUser, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.currentUser = currentUser;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String number){

        String username = currentUser.getUsername();

        Account account = accountRepository.findByAccountNumberAndOwnerUsername(number, username)
                .orElseThrow(AccountNotFoundException::new);

        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance()
        );
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {

        String username = currentUser.getUsername();

        User owner = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Account account = new Account(
                generateAccountNumber(),
                request.balance(),
                owner
        );

        accountRepository.save(account);

        return new AccountResponse(
                account.getAccountNumber(),
                account.getBalance());
    }

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}

package com.example.bank.service;

import com.example.bank.enums.Role;
import com.example.bank.integration.BaseIntegrationTest;
import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferServiceTest extends BaseIntegrationTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("samat");
        user.setPassword(passwordEncoder.encode("samat"));
        user.setRole(Role.USER);

        userRepository.save(user);

        accountRepository.save(new Account("A", new BigDecimal("1000"), user));
        accountRepository.save(new Account("B", new BigDecimal("500"), user));
    }

    @Test
    @WithMockUser(username = "samat", roles = "USER")
    void shouldTransferMoney() {
        transferService.transfer("A", "B", new BigDecimal("200"));

        Account from = accountRepository.findById("A").orElseThrow();
        Account to = accountRepository.findById("B").orElseThrow();

        assertEquals(new BigDecimal("800"), from.getBalance());
        assertEquals(new BigDecimal("700"), to.getBalance());
    }
}

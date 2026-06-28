package com.example.bank.service;

import com.example.bank.integration.BaseIntegrationTest;
import com.example.bank.repository.AccountRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends BaseIntegrationTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;
}

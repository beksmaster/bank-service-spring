package com.example.bank;

import com.example.bank.dto.AccountRequest;
import com.example.bank.dto.AccountResponse;
import com.example.bank.exception.AccountAlreadyExistsException;
import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import com.example.bank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldCreateAccount_whenAccountDoesNotExist() {
        AccountRequest request = new AccountRequest(
                "A100",
                BigDecimal.valueOf(1000)
        );
        when(accountRepository.existsById("A100")).thenReturn(false);

        AccountResponse response = accountService.createAccount(request);

        assertEquals("A100", response.accountNumber());

        assertEquals(BigDecimal.valueOf(1000),response.balance());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_shouldThrowException_whenAccountAlreadyExists() {
        AccountRequest request = new AccountRequest(
                "A100",
                BigDecimal.valueOf(1000)
        );
        when(accountRepository.existsById("A100")).thenReturn(true);

        assertThrows(
                AccountAlreadyExistsException.class,
                () -> accountService.createAccount(request)
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }


}

package com.example.bank.service;

import com.example.bank.dto.TransactionResponse;
import com.example.bank.enums.TransactionStatus;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {
    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    @Test
    void getById_shouldReturnTransaction() {
        Account from = new Account(
                "A100",
                BigDecimal.valueOf(1000)
        );
        Account to = new Account(
                "A200",
                BigDecimal.valueOf(500)
        );

        Transaction transaction = new Transaction(
                from,
                to,
                BigDecimal.valueOf(200),
                TransactionStatus.COMPLETED
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(transaction));

        TransactionResponse response = service.getById(1L);

        assertEquals(
                "A100",
                response.fromAccount()
        );

        assertEquals(
                "A200",
                response.toAccount()
        );

        assertEquals(
                BigDecimal.valueOf(200),
                response.amount()
        );

        verify(repository).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenTransactionNotFound() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> service.getById(1L));

        verify(repository)
                .findById(1L);
    }

    @Test
    void getByAccount_shouldReturnTransactions() {
        Account from = new Account(
                "A100",
                BigDecimal.valueOf(1000)
        );

        Account to = new Account(
                "B200",
                BigDecimal.valueOf(500)
        );
        Transaction transaction =
                new Transaction(
                        from,
                        to,
                        BigDecimal.valueOf(200),
                        TransactionStatus.COMPLETED
                );
        Page<Transaction> page =
                new PageImpl<>(
                        List.of(transaction)
                );

        Pageable pageable =
                PageRequest.of(0, 10);

        when(
                repository.findByFromAccountAccountNumberOrToAccountAccountNumber(
                        "A100",
                        "A100",
                        pageable
                )
        ).thenReturn(page);

        Page<TransactionResponse> response = service.getByAccount(
                "A100",
                pageable
        );
        TransactionResponse transaction1 = response.getContent().get(0);

        assertEquals(
                "A100",
                transaction1.fromAccount()
        );

        assertEquals(
                "B200",
                transaction1.toAccount()
        );

        assertEquals(
                1,
                response.getTotalElements()
        );

        verify(repository).findByFromAccountAccountNumberOrToAccountAccountNumber(
                "A100",
                "A100",
                pageable
        );
    }

    @Test
    void getByAccount_shouldReturnEmptyPage() {
        Pageable pageable =
                PageRequest.of(0, 10);

        when(
                repository.findByFromAccountAccountNumberOrToAccountAccountNumber(
                        "A100",
                        "A100",
                        pageable
                )).thenReturn(Page.empty());

        Page<TransactionResponse> response = service.getByAccount(
                "A100",
                pageable
        );

        assertTrue(response.isEmpty());

        assertEquals(0, response.getTotalElements());

        verify(repository)
                .findByFromAccountAccountNumberOrToAccountAccountNumber(
                        "A100",
                        "A100",
                        pageable
                );
    }
}

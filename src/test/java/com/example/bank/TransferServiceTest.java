package com.example.bank;

import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        Account a = new Account();
        a.setAccountNumber("A");
        a.setBalance(new BigDecimal("1000"));

        Account b = new Account();
        b.setAccountNumber("B");
        b.setBalance(new BigDecimal("500"));

        accountRepository.save(a);
        accountRepository.save(b);
    }

    @Test
    void shouldTransferMoney() {
        transferService.transfer("A", "B", new BigDecimal("200"));
        Account from = accountRepository.findById("A").get();
        Account to = accountRepository.findById("B").get();

        assertEquals(new BigDecimal("800"), from.getBalance());
        assertEquals(new BigDecimal("700"), to.getBalance());
    }

    @Test
    void shouldFailWhenNotEnoughMoney() {

        assertThrows(InsufficientFundsException.class, () ->
                transferService.transfer("A", "B", new BigDecimal("999999"))
        );
    }

    @Test
    void shouldFailWhenNegativeAmount() {
        assertThrows(InsufficientFundsException.class, () ->
                transferService.transfer("A", "B", new BigDecimal("-10"))
        );
    }

    @Test
    void shouldFailWhenTransferToSameAccount() {
        BigDecimal amount = new BigDecimal("100");

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        "A",
                        "A",
                        amount
                )
        );
    }
}

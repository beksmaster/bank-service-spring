package com.example.bank;

import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import com.example.bank.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class BankServiceTest {

    @Autowired
    private BankService bankService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
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
        bankService.transfer("A" , "B", new BigDecimal("200"));
        Account from = accountRepository.findById("A").get();
        Account to = accountRepository.findById("B").get();

        assertEquals(new BigDecimal("800"), from.getBalance());
        assertEquals(new BigDecimal("700"), to.getBalance());
    }

    @Test
    void shouldFailWhenNotEnoughMoney() {

        assertThrows(IllegalArgumentException.class, () ->
                bankService.transfer("A", "B", new BigDecimal("999999"))
        );
    }

    @Test
    void shouldFailWhenNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                bankService.transfer("A", "B", new BigDecimal("-10"))
        );
    }

    @Test
    void shouldRollbackWhenExceptionHappens() {

        assertThrows(RuntimeException.class, () -> {
            bankService.transfer("A", "B", new BigDecimal("100"));
            throw new RuntimeException("fail");
        });

        Account a = accountRepository.findById("A").get();

        // баланс должен остаться прежним
        assertEquals(new BigDecimal("1000"), a.getBalance());
    }

    @Test
    void shouldFindAccountsWithBalanceGreaterThanAmount() {
        List<Account> accounts = accountRepository.findByBalanceGreaterThan(new BigDecimal("600"));

        assertEquals(1, accounts.size());
        assertEquals("A", accounts.get(0).getAccountNumber());
    }

    @Test
    void shouldFindAccountsWithBalanceLessThanAmount() {
        List<Account> accounts = accountRepository.findByBalanceLessThan(new BigDecimal("600"));
        assertEquals(1, accounts.size());
        assertEquals("B", accounts.get(0).getAccountNumber());
    }

    @Test
    void shouldFindAccountsWithBetweenAmounts() {
        List<Account> accounts = accountRepository.findByBalanceBetween(
                new BigDecimal("400"),
                new BigDecimal("800")
        );

        assertEquals(1, accounts.size());
        assertEquals("B", accounts.get(0).getAccountNumber());
    }

    @Test
    void shouldFindAccountsByAccountNumberContainingText() {
        List<Account> accounts = accountRepository.findByAccountNumberContaining("A");

        assertEquals(1, accounts.size());
        assertEquals("A", accounts.get(0).getAccountNumber());
    }
}

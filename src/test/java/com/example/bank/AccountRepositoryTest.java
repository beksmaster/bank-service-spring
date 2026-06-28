package com.example.bank;

import com.example.bank.model.Account;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AccountRepositoryTest {
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

        Account c = new Account();
        c.setAccountNumber("C");
        c.setBalance(new BigDecimal("1500"));

        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);
    }

    @Test
    void shouldFindAccountsOrderedByBalanceDesc() {

        Account c = new Account();
        c.setAccountNumber("C");
        c.setBalance(new BigDecimal("1500"));
        accountRepository.save(c);

        List<Account> accounts = accountRepository.findByBalanceGreaterThanOrderByBalanceDesc(
                new BigDecimal("400")
        );

        assertEquals(3, accounts.size());
        assertEquals("C", accounts.get(0).getAccountNumber());
        assertEquals("A", accounts.get(1).getAccountNumber());
        assertEquals("B", accounts.get(2).getAccountNumber());
    }

    @Test
    void shouldFindRichAccountsByNumberText() {
        List<Account> accounts = accountRepository.findRichAccountsByNumberText(
                new BigDecimal("900"),
                "A"
        );

        assertEquals(1, accounts.size());
        assertEquals("A", accounts.get(0).getAccountNumber());
    }

    @Test
    void shouldFindAccountsByAccountNumberContainingText() {
        List<Account> accounts = accountRepository.findByAccountNumberContaining("A");

        assertEquals(1, accounts.size());
        assertEquals("A", accounts.get(0).getAccountNumber());
    }

    @Test
    void shouldFindRichAccounts() {
        List<Account> accounts = accountRepository.findRichAccounts(new BigDecimal("1000"));

        assertEquals(2, accounts.size());
        assertEquals("A", accounts.get(0).getAccountNumber());
    }


    @Test
    void shouldFindAccountsWithBalanceGreaterThanAmount() {
        List<Account> accounts = accountRepository.findByBalanceGreaterThan(new BigDecimal("600"));

        assertEquals(2, accounts.size());
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
}

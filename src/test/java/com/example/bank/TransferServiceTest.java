package com.example.bank;

import com.example.bank.enums.Role;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.Account;
import com.example.bank.model.User;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.repository.UserRepository;
import org.springframework.security.test.context.support.WithMockUser;
import com.example.bank.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {

        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("samat");
        user.setPassword("samat");
        user.setRole(Role.USER);
        userRepository.save(user);

        Account a = new Account("A", new BigDecimal("1000"), user);

        Account b = new Account("B", new BigDecimal("500"), user);

        accountRepository.save(a);
        accountRepository.save(b);
    }

    @Autowired
    DataSource dataSource;

    @Test
    void printDb() throws Exception {
        System.out.println(
                dataSource.getConnection().getMetaData().getURL()
        );
    }

    @Test
    @WithMockUser(username = "samat", roles = "USER")
    void shouldTransferMoney() {
        transferService.transfer("A", "B", new BigDecimal("200"));
        Account from = accountRepository.findById("A").get();
        Account to = accountRepository.findById("B").get();

        assertEquals(new BigDecimal("800"), from.getBalance());
        assertEquals(new BigDecimal("700"), to.getBalance());
    }

    @Test
    @WithMockUser(username = "samat", roles = "USER")
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

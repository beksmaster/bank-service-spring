package com.example.bank.repository;

import com.example.bank.model.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNumber = :number")
    Optional<Account> findByNumberForUpdate(String number);

    List<Account> findByBalanceGreaterThan(BigDecimal amount);

    List<Account> findByBalanceLessThan(BigDecimal amount);

    List<Account> findByBalanceBetween(BigDecimal min, BigDecimal max);

    List<Account> findByAccountNumberContaining(String text);
}
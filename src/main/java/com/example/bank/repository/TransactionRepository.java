package com.example.bank.repository;
import org.springframework.data.domain.Page;

import com.example.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT a from Transaction a where a.fromAccount.accountNumber = :number")
    Page<Transaction> findTransactionByAccountSender(String number, Pageable page);

    @Query("SELECT a from Transaction a where a.fromAccount.accountNumber = :number ORDER BY a.createdAt DESC LIMIT 10")
    List<Transaction> findLastTenTransactionsByAccountNumber(String number);

    Page<Transaction> findByFromAccountAccountNumberOrToAccountAccountNumber(
            String from,
            String to,
            Pageable pageable
    );

}

package com.example.bank.dto;

import com.example.bank.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        Instant createdAt,
        TransactionStatus status
) {
}

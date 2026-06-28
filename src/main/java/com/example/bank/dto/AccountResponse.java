package com.example.bank.dto;

import com.example.bank.model.User;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        BigDecimal balance) {
}

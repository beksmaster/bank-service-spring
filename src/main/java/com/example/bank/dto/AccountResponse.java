package com.example.bank.dto;

import java.math.BigDecimal;

public record AccountResponse(String accountNumber, BigDecimal balance) {
}

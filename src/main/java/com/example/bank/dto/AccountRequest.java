package com.example.bank.dto;

import java.math.BigDecimal;

public record AccountRequest(String accountNumber, BigDecimal balance) {
}

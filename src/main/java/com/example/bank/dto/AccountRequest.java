package com.example.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AccountRequest(
        @NotBlank
        String accountNumber,

        @PositiveOrZero
        BigDecimal balance) {

}

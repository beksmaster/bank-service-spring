package com.example.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank
        String fromAccount,

        @NotBlank
        String toAccount,

        @NotNull
        @Positive
        BigDecimal amount) {
}

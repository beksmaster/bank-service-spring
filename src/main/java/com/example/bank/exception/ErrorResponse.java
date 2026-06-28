package com.example.bank.exception;

public record ErrorResponse(
        java.time.Instant timestamp,
        int status,
        String message
) {
}

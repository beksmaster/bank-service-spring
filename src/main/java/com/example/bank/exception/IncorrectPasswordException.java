package com.example.bank.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super("Incorrect password");
    }
}

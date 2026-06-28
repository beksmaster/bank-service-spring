package com.example.bank.exception;

public class SelfTransferNotAllowedException extends RuntimeException {
    public SelfTransferNotAllowedException(String message) {
        super("Self transfer not allowed");
    }
}

package com.example.bank.exception;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException () {
        super("Transaction not found.");
    }
}

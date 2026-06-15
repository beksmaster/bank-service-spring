package com.example.bank.service;

import com.example.bank.controller.TransactionController;
import com.example.bank.model.Transaction;
import com.example.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction getById(Long id){
        return null;
    }
}

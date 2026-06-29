package com.example.bank.service;
import com.example.bank.dto.TransactionResponse;

import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.model.Transaction;
import com.example.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionResponse getById(Long id){
        Transaction transaction =
                repository.findById(id)
                        .orElseThrow(TransactionNotFoundException::new);
        return toResponse(transaction);
    }

    private TransactionResponse toResponse(Transaction transaction){
        return new TransactionResponse(
                transaction.getId(),
                transaction.getFromAccount().getAccountNumber(),
                transaction.getToAccount().getAccountNumber(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getStatus()
        );
    }

    public Page<TransactionResponse> getByAccount (
            String accountNumber,
            Pageable pageable
    ){
        return repository.findByFromAccountAccountNumberOrToAccountAccountNumber(
                accountNumber,
                accountNumber,
                pageable
        ).map(this::toResponse);
    }
}

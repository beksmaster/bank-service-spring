package com.example.bank.controller;

import com.example.bank.config.ApiPaths;
import com.example.bank.dto.TransactionResponse;
import com.example.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.API_V1 +"/transactions")
@Tag(name = "Transactions", description = "Transaction history operations")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public TransactionResponse getById(
            @PathVariable Long id
    ) {
        return service.getById(id);
    }
    //TODO сделать геттер по юзеру

    @GetMapping("/account/{number}")
    public Page<TransactionResponse> getByAccount(
            @PathVariable String number,
            @ParameterObject Pageable pageable
            ) {
        return service.getByAccount(number, pageable);
    }
}

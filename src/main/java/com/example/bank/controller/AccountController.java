package com.example.bank.controller;

import com.example.bank.config.ApiPaths;
import com.example.bank.dto.AccountRequest;
import com.example.bank.dto.AccountResponse;
import com.example.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( ApiPaths.API_V1 +"/accounts")
@Tag(name = "Accounts", description = "Account management operations")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create Account")
    @PostMapping("/create")
    public AccountResponse createAccount(
            @RequestBody AccountRequest accountRequest
            ){
        return accountService.createAccount(accountRequest);
    }

    @Operation(summary = "Get Account by number")
    @GetMapping("/{number}")
    public AccountResponse getAccount(
            @PathVariable String number) {
        return accountService.getAccount(number);
    }
}

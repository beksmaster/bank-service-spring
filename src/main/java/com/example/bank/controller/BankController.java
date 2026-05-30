package com.example.bank.controller;

import com.example.bank.service.BankService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class BankController {

    private final BankService service;

    public BankController(BankService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam String from,
                         @RequestParam String to,
                         @RequestParam BigDecimal amount) {
        service.transfer(from, to, amount);
    }
}
package com.example.bank.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Tag(name = "Bank API", description = "Operation with transfers")
public class BankController {
}
package com.example.bank.controller;
import com.example.bank.dto.AccountResponse;
import com.example.bank.dto.TransferRequest;
import com.example.bank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Tag(name = "Bank API", description = "Operation with accounts and transfers")
public class BankController {

    private final BankService service;

    public BankController(BankService service) {
        this.service = service;
    }

    @Operation(summary = "Get Account by number")
    @GetMapping("/{number}")
    public AccountResponse getAccount(
            @PathVariable String number) {
        return service.getAccount(number);
    }

    @Operation(summary = "Transfer money between accounts")
    @PostMapping("/transfer")
    public void transfer(
           @Valid @RequestBody TransferRequest request){
        service.transfer(
                request.from(),
                request.to(),
                request.amount()
        );
    }
}
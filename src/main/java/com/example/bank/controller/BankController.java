package com.example.bank.controller;
import com.example.bank.dto.AccountResponse;
import com.example.bank.dto.TransferRequest;
import com.example.bank.service.BankService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class BankController {

    private final BankService service;

    public BankController(BankService service) {
        this.service = service;
    }
    @GetMapping("/{number}")
    public AccountResponse getAccount(
            @PathVariable String number) {
        return service.getAccount(number);
    }

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
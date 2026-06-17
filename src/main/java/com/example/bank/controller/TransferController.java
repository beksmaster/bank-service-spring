package com.example.bank.controller;

import com.example.bank.config.ApiPaths;
import com.example.bank.dto.TransferRequest;
import com.example.bank.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Transfers", description = "Operation with transfers")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/transfers")
public class TransferController {
    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;

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

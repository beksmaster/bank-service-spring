package com.example.bank.controller;

import com.example.bank.dto.LoginRequest;
import com.example.bank.dto.LoginResponse;
import com.example.bank.dto.RegisterRequest;
import com.example.bank.dto.RegisterResponse;
import com.example.bank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register (
            @Valid @RequestBody RegisterRequest request
            ) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}

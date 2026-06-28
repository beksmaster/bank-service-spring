package com.example.bank.service;

import com.example.bank.dto.LoginRequest;
import com.example.bank.dto.LoginResponse;
import com.example.bank.dto.RegisterRequest;
import com.example.bank.dto.RegisterResponse;
import com.example.bank.model.User;
import com.example.bank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.bank.enums.Role.USER;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(USER);

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole().name()
        );

    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(
                request.username()).orElseThrow(
                () -> new RuntimeException("Username not found.")
        );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new RuntimeException("Password is incorrect");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);

    }
}

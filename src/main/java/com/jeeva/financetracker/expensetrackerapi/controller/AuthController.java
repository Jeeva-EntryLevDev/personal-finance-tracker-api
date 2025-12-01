package com.jeeva.financetracker.expensetrackerapi.controller;

import com.jeeva.financetracker.expensetrackerapi.auth.AuthService;
import com.jeeva.financetracker.expensetrackerapi.dto.AuthResponse;
import com.jeeva.financetracker.expensetrackerapi.dto.LoginRequest;
import com.jeeva.financetracker.expensetrackerapi.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

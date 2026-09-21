package com.example.bookapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.bookapi.auth.AuthRequest;
import com.example.bookapi.auth.AuthResponse;
import com.example.bookapi.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @Valid @RequestBody AuthRequest request) {

        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody AuthRequest request) {

        return authService.login(request);
    }

    // just for trial, using refresh param. In production, not recommended as we are exposing it in the URL.
    @PostMapping("/refresh")
    public AuthResponse refresh(
            @RequestParam String refreshToken) {

        return authService.refreshAccessToken(refreshToken);
    }
}
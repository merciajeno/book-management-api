package com.example.bookapi.service;

import com.example.bookapi.auth.AuthRequest;
import com.example.bookapi.auth.AuthResponse;
import com.example.bookapi.auth.RefreshToken;
import com.example.bookapi.repository.UserRepository;
import com.example.bookapi.user.User;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            org.springframework.security.core.userdetails.UserDetailsService userDetailsService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public void register(AuthRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getUsername()
                );

        String accessToken =
                jwtService.generateToken(userDetails);

        User user =
                userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public AuthResponse refreshAccessToken(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(refreshTokenValue);

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        user.getUsername()
                );

        String newAccessToken =
                jwtService.generateToken(userDetails);

        return new AuthResponse(
                newAccessToken,
                refreshToken.getToken()
        );
    }
}
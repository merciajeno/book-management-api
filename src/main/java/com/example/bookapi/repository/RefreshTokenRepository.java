package com.example.bookapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookapi.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(Long userId);
}
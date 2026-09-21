package com.example.bookapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookapi.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}

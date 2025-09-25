package com.example.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findUserByVerificationCode(String code);
}

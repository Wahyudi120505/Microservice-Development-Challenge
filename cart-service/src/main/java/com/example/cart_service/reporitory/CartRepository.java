package com.example.cart_service.reporitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cart_service.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(Integer userId);
}
package com.example.cart_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.cart_service.dto.GenericResponse;

@FeignClient(name = "user-service", url = "http://localhost:8080/auth/")
public interface UserClient {
    @GetMapping("/user/{id}")
    GenericResponse<Object> checkUserExists(@PathVariable("id") Integer id);
}
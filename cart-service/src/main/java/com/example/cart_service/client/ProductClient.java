package com.example.cart_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.cart_service.dto.GenericResponse;
import com.example.cart_service.dto.ProductResponse;


@FeignClient(name = "product-service", url = "http://localhost:8081/products")
public interface ProductClient {
    @GetMapping("/get/{id}")
    GenericResponse<ProductResponse> getProductById(@PathVariable("id") Integer id);
}
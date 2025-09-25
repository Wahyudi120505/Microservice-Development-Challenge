package com.example.product_service.service.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.product_service.dto.product.ProductRequest;
import com.example.product_service.dto.product.ProductResponse;

public interface ProductService {
    void create(ProductRequest request, MultipartFile image);
    void update(Integer id, ProductRequest request, MultipartFile image);
    void delete(Integer id);
    ProductResponse getById(Integer id);
    List<ProductResponse> getAll();
}

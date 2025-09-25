package com.example.product_service.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String nama;
    private Integer price;
    private Integer quantity;
    private String desc;
    private String category;
}

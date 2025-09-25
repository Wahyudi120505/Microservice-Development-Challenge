package com.example.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Integer id;
    private String nama;
    private Integer price;
    private Integer quantity;
    private String desc;
    private String category;
    private String status;
    private Boolean deleted;
    private String photo;
}

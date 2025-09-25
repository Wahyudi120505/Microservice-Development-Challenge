package com.example.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailCartItemResponse {
    private Integer productId;
    private Integer quantity;
    private Integer price;
    private Integer subtotal;
}

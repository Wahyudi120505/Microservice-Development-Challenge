package com.example.cart_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Integer userId;
    private LocalDateTime purchaseDate;
    private Long totalPrice;
    private List<DetailCartItemResponse> items;
}

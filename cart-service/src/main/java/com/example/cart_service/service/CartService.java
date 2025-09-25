package com.example.cart_service.service;

import java.util.List;

import com.example.cart_service.dto.AddCartRequest;
import com.example.cart_service.dto.CartResponse;

public interface CartService {
    CartResponse addCart(List<AddCartRequest> request);
    CartResponse updateCartItem(AddCartRequest request);
    CartResponse deleteCartItem( Integer productId);
    CartResponse getCartByUser();
}

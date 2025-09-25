package com.example.cart_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cart_service.client.ProductClient;
import com.example.cart_service.client.UserClient;
import com.example.cart_service.dto.AddCartRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.dto.DetailCartItemResponse;
import com.example.cart_service.dto.GenericResponse;
import com.example.cart_service.dto.ProductResponse;
import com.example.cart_service.model.Cart;
import com.example.cart_service.model.CartItem;
import com.example.cart_service.reporitory.CartRepository;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public CartServiceImpl(UserClient userClient, ProductClient productClient, CartRepository cartRepository) {
        this.userClient = userClient;
        this.productClient = productClient;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartResponse addCart(List<AddCartRequest> requestList) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userClient.checkUserExists(userId).isSuccess()) {
            throw new RuntimeException("User tidak ditemukan");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserId(userId);
                    c.setPurchaseDate(LocalDateTime.now());
                    c.setItems(new ArrayList<>());
                    c.setTotalPrice(0L);
                    return c;
                });

        for (AddCartRequest request : requestList) {
            // cek produk ke product-service
            GenericResponse<ProductResponse> productResponse = productClient.getProductById(request.getProductId());
            if (!productResponse.isSuccess() || productResponse.getData() == null) {
                throw new RuntimeException(
                        "Produk dengan ID " + request.getProductId() + " tidak ditemukan di Product Service");
            }

            var product = productResponse.getData();
            Integer unitPrice = product.getPrice();
            if (unitPrice == null) {
                throw new RuntimeException("Unit price produk " + product.getNama() + " tidak tersedia");
            }

            // cek apakah produk sudah ada di cart
            CartItem existingItem = cart.getItems().stream()
                    .filter(i -> i.getProductId().equals(request.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                existingItem.setSubtotal(existingItem.getQuantity() * unitPrice);
            } else {
                CartItem newItem = new CartItem();
                newItem.setProductId(request.getProductId());
                newItem.setQuantity(request.getQuantity());
                newItem.setUnitPrice(unitPrice);
                newItem.setSubtotal(unitPrice * request.getQuantity());
                cart.getItems().add(newItem);
            }
        }

        cart.setTotalPrice(cart.getItems().stream().mapToLong(CartItem::getSubtotal).sum());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(AddCartRequest request) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart tidak ditemukan"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item tidak ditemukan"));

        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity harus lebih dari 0");
        }

        item.setQuantity(request.getQuantity());
        item.setSubtotal(item.getQuantity() * item.getUnitPrice());
        cart.setTotalPrice(cart.getItems().stream().mapToLong(CartItem::getSubtotal).sum());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    public CartResponse deleteCartItem(Integer productId) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart tidak ditemukan"));

        boolean removed = cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        if (!removed) {
            throw new RuntimeException("Item tidak ditemukan");
        }

        cart.setTotalPrice(cart.getItems().stream().mapToLong(CartItem::getSubtotal).sum());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Override
    public CartResponse getCartByUser() {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart tidak ditemukan"));
        return toCartResponse(cart);
    }

    private CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setUserId(cart.getUserId());
        response.setPurchaseDate(cart.getPurchaseDate());
        response.setTotalPrice(cart.getTotalPrice());

        List<DetailCartItemResponse> itemResponses = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            DetailCartItemResponse itemResp = new DetailCartItemResponse();
            itemResp.setProductId(item.getProductId());
            itemResp.setQuantity(item.getQuantity());
            itemResp.setPrice(item.getUnitPrice());
            itemResp.setSubtotal(item.getSubtotal());;
            itemResponses.add(itemResp);
        }
        response.setItems(itemResponses);

        return response;
    }
}

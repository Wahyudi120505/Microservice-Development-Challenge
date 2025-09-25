package com.example.cart_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.cart_service.dto.AddCartRequest;
import com.example.cart_service.dto.GenericResponse;
import com.example.cart_service.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Object> addCart(@RequestBody List<AddCartRequest> request) {
        try {
            return ResponseEntity.ok().body(
                    GenericResponse.success(cartService.addCart(request), "Berhasil menambahkan ke keranjang"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/edit-cart")
    public ResponseEntity<Object> updateCartItem(@RequestBody AddCartRequest request) {
        try {
            return ResponseEntity.ok().body(
                    GenericResponse.success(cartService.updateCartItem(request), "Berhasil update item"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Object> deleteCartItem(@PathVariable Integer productId) {
        try {
            return ResponseEntity.ok().body(
                    GenericResponse.success(cartService.deleteCartItem(productId), "Berhasil hapus item"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getCart() {
        try {
            return ResponseEntity.ok().body(
                    GenericResponse.success(cartService.getCartByUser(), "Berhasil ambil keranjang"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}

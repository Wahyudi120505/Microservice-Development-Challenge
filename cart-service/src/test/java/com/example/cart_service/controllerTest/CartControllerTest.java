package com.example.cart_service.controllerTest;


import com.example.cart_service.controller.CartController;
import com.example.cart_service.dto.AddCartRequest;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.security.JwtUtil;
import com.example.cart_service.service.CartService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void testAddCart() throws Exception {
        AddCartRequest req = new AddCartRequest();
        req.setProductId(1);
        req.setQuantity(2);

        CartResponse mockResponse = new CartResponse();
        mockResponse.setUserId(9);
        mockResponse.setPurchaseDate(LocalDateTime.now());
        mockResponse.setTotalPrice(20000L);

        when(cartService.addCart(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"productId\":1,\"quantity\":2}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Berhasil menambahkan ke keranjang"));
    }

    @Test
    void testUpdateCartItem() throws Exception {
        CartResponse mockResponse = new CartResponse();
        mockResponse.setUserId(9);
        mockResponse.setTotalPrice(50000L);

        when(cartService.updateCartItem(any())).thenReturn(mockResponse);

        mockMvc.perform(put("/cart/edit-cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":1,\"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Berhasil update item"));
    }

    @Test
    void testDeleteCartItem() throws Exception {
        CartResponse mockResponse = new CartResponse();
        mockResponse.setUserId(9);
        mockResponse.setTotalPrice(0L);

        when(cartService.deleteCartItem(1)).thenReturn(mockResponse);

        mockMvc.perform(delete("/cart/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Berhasil hapus item"));
    }

    @Test
    void testGetCart() throws Exception {
        CartResponse mockResponse = new CartResponse();
        mockResponse.setUserId(9);
        mockResponse.setTotalPrice(100000L);

        when(cartService.getCartByUser()).thenReturn(mockResponse);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Berhasil ambil keranjang"))
                .andExpect(jsonPath("$.data.totalPrice").value(100000));
    }
}

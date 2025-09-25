package com.example.product_service.controllerTest;

import com.example.product_service.controllers.ProductController;
import com.example.product_service.dto.product.ProductRequest;
import com.example.product_service.dto.product.ProductResponse;
import com.example.product_service.security.JwtUtil;
import com.example.product_service.service.product.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void testCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setNama("Produk A");
        request.setPrice(10000);
        request.setQuantity(5);
        request.setCategory("Kategori 1");
        request.setDesc("Deskripsi produk");

        MockMultipartFile file = new MockMultipartFile(
                "Product Image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image".getBytes());

        doNothing().when(productService).create(any(ProductRequest.class), any());

        mockMvc.perform(multipart("/products/create")
                .file(file)
                .param("nama", request.getNama())
                .param("price", String.valueOf(request.getPrice()))
                .param("quantity", String.valueOf(request.getQuantity()))
                .param("category", request.getCategory())
                .param("desc", request.getDesc()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateProduct() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "Product Image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes());

        mockMvc.perform(multipart("/products/update/{id}", 1)
                .file(imageFile)
                .param("name", "Produk Baru")
                .param("price", "10000")
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                }) 
        )
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProduct() throws Exception {
        int id = 1;
        doNothing().when(productService).delete(id);

        mockMvc.perform(delete("/products/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetProductById() throws Exception {
        int id = 1;
        ProductResponse product = ProductResponse.builder().nama("Produk C").build();
        when(productService.getById(id)).thenReturn(product);

        mockMvc.perform(get("/products/get/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nama").value("Produk C"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductResponse product1 = ProductResponse.builder().nama("P1").build();
        ProductResponse product2 = ProductResponse.builder().nama("P2").build();
        when(productService.getAll()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nama").value("P1"))
                .andExpect(jsonPath("$.data[1].nama").value("P2"));
    }
}

package com.example.product_service.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.product_service.dto.GenericResponse;
import com.example.product_service.dto.product.ProductRequest;
import com.example.product_service.service.product.ProductService;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> create(ProductRequest prequest,
            @RequestParam("Product Image") MultipartFile file) {
        try {
            productService.create(prequest, file);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Success Add New Product"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> update(
            @PathVariable int id,
            ProductRequest request,
            @RequestParam("Product Image") MultipartFile image) {
        try {
            productService.update(id, request, image);
            return ResponseEntity.ok(GenericResponse.success(null, "Produk berhasil diperbarui"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    // Delete Product (soft delete)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok(GenericResponse.success(null, "Produk berhasil dihapus!"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(GenericResponse.success(productService.getById(id), "Produk ditemukan"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllProducts() {
        try {
            return ResponseEntity.ok(GenericResponse.success(productService.getAll(), "Daftar produk berhasil diambil"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}

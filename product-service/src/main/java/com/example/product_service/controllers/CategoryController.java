package com.example.product_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.product_service.dto.GenericResponse;
import com.example.product_service.dto.category.CategoryDto;
import com.example.product_service.service.category.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Object> getAllCategories() {
        try {
            return ResponseEntity.ok(GenericResponse.success(categoryService.getAll(), "Sukses"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(GenericResponse.success(categoryService.getById(id), "Sukses"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody CategoryDto category) {
        try {
            categoryService.create(category);
            return ResponseEntity.ok(GenericResponse.success(null, "Kategori berhasil dibuat"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto category) {
        try {
            categoryService.update(id, category);
            return ResponseEntity.ok(GenericResponse.success(null, "Kategori berhasil diperbarui"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.ok(GenericResponse.success(null, "Kategori berhasil dihapus"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()));
        }
    }
}

package com.example.product_service.service.category;

import java.util.List;

import com.example.product_service.dto.category.CategoryDto;

public interface CategoryService {
    void create(CategoryDto category);
    void update(Integer id, CategoryDto category);
    void delete(Integer id);
    CategoryDto getById(Integer id);
    List<CategoryDto> getAll();
}
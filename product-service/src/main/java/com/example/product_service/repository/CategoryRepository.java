package com.example.product_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product_service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findCategoryByName(String nama);
    Category findByNameIgnoreCase(String name);
}

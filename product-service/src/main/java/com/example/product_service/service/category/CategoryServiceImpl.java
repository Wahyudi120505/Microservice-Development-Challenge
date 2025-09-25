package com.example.product_service.service.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product_service.dto.category.CategoryDto;
import com.example.product_service.model.Category;
import com.example.product_service.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void create(CategoryDto category) {
        if(category.getName() == null || category.getName().isBlank()) {
            throw new RuntimeException("Nama kategori tidak boleh kosong!");
        }

        Category exist = categoryRepository.findByNameIgnoreCase(category.getName());
        if(exist != null) {
            throw new RuntimeException("Kategori sudah ada: " + category.getName());
        }

        Category saveCategory = new Category();
        saveCategory.setName(category.getName());
        categoryRepository.save(saveCategory);
    }

    @Override
    public void update(Integer id, CategoryDto category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan dengan id: " + id));

        if(category.getName() != null && !category.getName().isBlank()) {
            existing.setName(category.getName());
        }

        categoryRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan dengan id: " + id));
        categoryRepository.delete(existing);
    }

@Override
public CategoryDto getById(Integer id) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan dengan id: " + id));
    return CategoryDto.builder()
            .name(category.getName())
            .build();
}

@Override
public List<CategoryDto> getAll() {
    return categoryRepository.findAll().stream()
            .map(category -> CategoryDto.builder()
                    .name(category.getName())
                    .build())
            .collect(Collectors.toList());
}
}

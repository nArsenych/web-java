package com.example.spacecatsmarket.web.controller;

import com.example.spacecatsmarket.domain.category.Category;
import com.example.spacecatsmarket.dto.category.CategoryDto;
import com.example.spacecatsmarket.service.interfaces.CategoryService;
import com.example.spacecatsmarket.web.mapper.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        Category category = categoryService.create(categoryMapper.toCategory(categoryDto));
        return ResponseEntity.ok(categoryMapper.toCategoryDto(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAll().stream()
                .map(categoryMapper::toCategoryDto)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        Category category = categoryService.getById(id);
        return ResponseEntity.ok(categoryMapper.toCategoryDto(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id, @RequestBody @Valid CategoryDto categoryDto) {
        Category updatedCategory = categoryService.update(id, categoryMapper.toCategory(categoryDto));
        return ResponseEntity.ok(categoryMapper.toCategoryDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

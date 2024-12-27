package com.example.spacecatsmarket.service.interfaces;


import com.example.spacecatsmarket.domain.category.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category create(Category category);
    Category getById(UUID id);
    List<Category> getAll();
    Category update(UUID id, Category category);
    void delete(UUID id);
}

package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.AbstractIt;
import com.example.spacecatsmarket.domain.category.Category;
import com.example.spacecatsmarket.service.exception.CategoryNotFoundException;
import com.example.spacecatsmarket.service.interfaces.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Testcontainers
@DisplayName("Category Service Integration Test")
class CategoryServiceTest extends AbstractIt {

    @Autowired
    private CategoryService categoryService;

    private static final String UPDATED_CATEGORY_NAME = "Updated Cosmic Accessories";

    private static final Category DEFAULT_CATEGORY = Category.builder()
            .name("Cosmic Accessories")
            .description("Category for all cosmic accessories.")
            .build();

    @BeforeEach
    void resetDatabase() {
        categoryService.getAll().forEach(category -> categoryService.delete(category.getId()));
        System.out.println("Database cleaned up. Remaining categories: " + categoryService.getAll().size());
    }

    @Test
    void shouldCreateCategory() {
        Category createdCategory = categoryService.create(DEFAULT_CATEGORY);

        assertNotNull(createdCategory, "Created category should not be null.");
        assertEquals(DEFAULT_CATEGORY.getName(), createdCategory.getName(), "Category names do not match.");
        assertEquals(DEFAULT_CATEGORY.getDescription(), createdCategory.getDescription(), "Category descriptions do not match.");
    }

    @Test
    void shouldReturnAllCategories() {
        categoryService.create(DEFAULT_CATEGORY);
        List<Category> categories = categoryService.getAll();

        assertNotNull(categories, "The category list should not be null.");
        assertFalse(categories.isEmpty(), "The category list should not be empty.");
        assertEquals(1, categories.size(), "The size of the category list does not match the expected value.");
    }

    @Test
    void shouldGetCategoryById() {
        Category createdCategory = categoryService.create(DEFAULT_CATEGORY);
        Category retrievedCategory = categoryService.getById(createdCategory.getId());

        assertNotNull(retrievedCategory, "Retrieved category should not be null.");
        assertEquals(DEFAULT_CATEGORY.getName(), retrievedCategory.getName(), "Retrieved category name does not match.");
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFound() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(UUID.randomUUID()),
                "Expected exception not thrown for non-existent category ID.");
    }

    @Test
    void shouldUpdateCategory() {
        Category createdCategory = categoryService.create(DEFAULT_CATEGORY);

        Category updatedCategory = Category.builder()
                .id(createdCategory.getId())
                .name(UPDATED_CATEGORY_NAME)
                .description("Updated description.")
                .build();

        Category result = categoryService.update(createdCategory.getId(), updatedCategory);

        assertNotNull(result, "Updated category should not be null.");
        assertEquals(UPDATED_CATEGORY_NAME, result.getName(), "Updated category name does not match.");
        assertEquals("Updated description.", result.getDescription(), "Updated category description does not match.");
    }

    @Test
    void shouldDeleteCategory() {
        Category createdCategory = categoryService.create(DEFAULT_CATEGORY);

        categoryService.delete(createdCategory.getId());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(createdCategory.getId()),
                "Expected exception not thrown for deleted category ID.");
    }
}

package com.example.spacecatsmarket.service.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category with id %s not found";

    public CategoryNotFoundException(UUID categoryId) {
        super(String.format(CATEGORY_NOT_FOUND_MESSAGE, categoryId));
    }
}

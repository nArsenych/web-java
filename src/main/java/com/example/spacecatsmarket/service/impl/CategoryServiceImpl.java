package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.category.Category;
import com.example.spacecatsmarket.repository.CategoryRepository;
import com.example.spacecatsmarket.repository.entity.CategoryEntity;
import com.example.spacecatsmarket.service.exception.CategoryNotFoundException;
import com.example.spacecatsmarket.service.interfaces.CategoryService;
import com.example.spacecatsmarket.web.mapper.CategoryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category create(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new DataIntegrityViolationException("Category with name '" + category.getName() + "' already exists.");
        }
        CategoryEntity entity = categoryMapper.toCategoryEntity(category);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        log.info("Category created: {}", savedEntity);
        return categoryMapper.toCategory(savedEntity);
    }

    @Override
    public Category getById(UUID id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.toCategory(entity);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategory)
                .collect(Collectors.toList());
    }

    @Override
    public Category update(UUID id, Category updatedCategory) {
        CategoryEntity existingEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        existingEntity.setName(updatedCategory.getName());
        existingEntity.setDescription(updatedCategory.getDescription());
        CategoryEntity savedEntity = categoryRepository.save(existingEntity);

        return categoryMapper.toCategory(savedEntity);
    }

    @Override
    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}

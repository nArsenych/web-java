package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.product.Product;
import com.example.spacecatsmarket.repository.CategoryRepository;
import com.example.spacecatsmarket.repository.ProductRepository;
import com.example.spacecatsmarket.repository.entity.CategoryEntity;
import com.example.spacecatsmarket.repository.entity.ProductEntity;
import com.example.spacecatsmarket.service.exception.ProductNotFoundException;
import com.example.spacecatsmarket.service.interfaces.ProductService;
import com.example.spacecatsmarket.web.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public Product createProduct(Product product) {
        ProductEntity entity = productMapper.toProductEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        log.info("Product created with ID: {}", savedEntity.getId());
        return productMapper.toProduct(savedEntity);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProduct);
    }

    @Override
    public boolean existsById(Long productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product updateProduct(Long productId, Product updatedProduct) {
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        entity.setName(updatedProduct.getName());
        entity.setDescription(updatedProduct.getDescription());
        entity.setPrice(updatedProduct.getPrice());
        entity.setStock(updatedProduct.getStock());

        CategoryEntity categoryEntity = categoryRepository.findByName(updatedProduct.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found: " + updatedProduct.getCategory()));
        entity.setCategory(categoryEntity);

        ProductEntity savedEntity = productRepository.save(entity);
        log.info("Product updated with ID: {}", productId);
        return productMapper.toProduct(savedEntity);
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
        log.info("Product deleted with ID: {}", productId);
    }
}

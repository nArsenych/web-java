package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.product.Product;
import com.example.spacecatsmarket.service.interfaces.ProductService;
import com.example.spacecatsmarket.service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final Map<UUID, Product> productRepository = new HashMap<>();

    @Override
    public Product createProduct(Product product) {
        productRepository.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> getProductById(UUID productId) {
        return Optional.ofNullable(Optional.ofNullable(productRepository.get(productId))
                .orElseThrow(() -> new ProductNotFoundException(productId)));
    }

    @Override
    public boolean existsById(UUID productId) {
        return productRepository.containsKey(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productRepository.values());
    }

    @Override
    public Product updateProduct(UUID productId, Product updatedProduct) {
        if (!productRepository.containsKey(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.put(productId, updatedProduct);
        return updatedProduct;
    }

    @Override
    public void deleteProduct(UUID productId) {
        if (!productRepository.containsKey(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.remove(productId);
    }
}

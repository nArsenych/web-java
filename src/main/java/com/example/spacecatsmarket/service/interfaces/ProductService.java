package com.example.spacecatsmarket.service.interfaces;

import com.example.spacecatsmarket.domain.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getProductById(UUID productId);

    boolean existsById(UUID productId);

    List<Product> getAllProducts();

    Product updateProduct(UUID productId, Product updatedProduct);

    void deleteProduct(UUID productId);
}

package com.example.spacecatsmarket.service.interfaces;

import com.example.spacecatsmarket.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getProductById(Long productId);

    boolean existsById(Long productId);

    List<Product> getAllProducts();

    Product updateProduct(Long productId, Product updatedProduct);

    void deleteProduct(Long productId);
}

package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.AbstractIt;
import com.example.spacecatsmarket.config.MappersTestConfiguration;
import com.example.spacecatsmarket.domain.product.Product;
import com.example.spacecatsmarket.repository.CategoryRepository;
import com.example.spacecatsmarket.repository.ProductRepository;
import com.example.spacecatsmarket.repository.entity.CategoryEntity;
import com.example.spacecatsmarket.repository.entity.ProductEntity;
import com.example.spacecatsmarket.service.exception.ProductNotFoundException;
import com.example.spacecatsmarket.service.interfaces.ProductService;
import com.example.spacecatsmarket.web.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
@DisplayName("Product Service Tests")
@Import(MappersTestConfiguration.class)
class ProductServiceTest extends AbstractIt {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    private static UUID PRODUCT_ID;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        CategoryEntity categoryEntity = CategoryEntity.builder()
                .name("Test Category")
                .description("Test Category Description")
                .build();
        categoryEntity = categoryRepository.save(categoryEntity);

        ProductEntity productEntity = ProductEntity.builder()
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .stock(100)
                .category(categoryEntity)
                .build();
        productEntity = productRepository.save(productEntity);

        PRODUCT_ID = productEntity.getId();
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productService.getAllProducts();

        assertNotNull(products, "Product list should not be null.");
        assertFalse(products.isEmpty(), "Product list should not be empty.");
    }

    @Test
    void shouldGetProductById() {
        Product product = productService.getProductById(PRODUCT_ID);

        assertNotNull(product, "Product should not be null.");
        assertEquals(PRODUCT_ID, product.getId(), "Product ID should match the expected value.");
        assertEquals("Test Product", product.getName(), "Product name should match the expected value.");
    }

    @Test
    void shouldCreateProduct() {
        CategoryEntity newCategory = CategoryEntity.builder()
                .name("New Category")
                .description("New Category Description")
                .build();
        newCategory = categoryRepository.save(newCategory);

        Product newProduct = Product.builder()
                .name("New Product")
                .description("New Description")
                .price(120.0)
                .stock(50)
                .category(newCategory.getName())
                .build();

        Product createdProduct = productService.createProduct(newProduct);

        assertNotNull(createdProduct);
        assertEquals("New Product", createdProduct.getName());
        assertEquals("New Category", createdProduct.getCategory());
    }

    @Test
    void shouldUpdateProduct() {
        CategoryEntity updatedCategory = CategoryEntity.builder()
                .name("Updated Category")
                .description("Updated Category Description")
                .build();
        updatedCategory = categoryRepository.save(updatedCategory);

        Product updatedProduct = Product.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(150.0)
                .stock(80)
                .category(updatedCategory.getName())
                .build();

        Product result = productService.updateProduct(PRODUCT_ID, updatedProduct);

        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals("Updated Category", result.getCategory());
    }

    @Test
    void shouldDeleteProduct() {
        productService.deleteProduct(PRODUCT_ID);

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(PRODUCT_ID));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(nonExistentId));
    }
}

package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.config.MappersTestConfiguration;
import com.example.spacecatsmarket.config.TestRestClientConfiguration;
import com.example.spacecatsmarket.config.WireMockConfig;
import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.dto.order.OrderEntryDto;
import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.repository.CategoryRepository;
import com.example.spacecatsmarket.repository.ProductRepository;
import com.example.spacecatsmarket.repository.entity.CategoryEntity;
import com.example.spacecatsmarket.repository.entity.ProductEntity;
import com.example.spacecatsmarket.service.interfaces.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import({MappersTestConfiguration.class, TestRestClientConfiguration.class, WireMockConfig.class})
@DisplayName("Order Service Tests")
@TestMethodOrder(OrderAnnotation.class)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final UUID CART_ID = UUID.randomUUID();
    private static final String CUSTOMER_REFERENCE = "customerRef";
    private static final double TOTAL_PRICE = 105.0;

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
                .name("ASTRO_MOUSE_TOYS")
                .description("Test Description")
                .price(10.0)
                .stock(100)
                .category(categoryEntity)
                .build();

        productRepository.save(productEntity);
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        PlaceOrderRequestDto requestDto = PlaceOrderRequestDto.builder()
                .totalPrice(TOTAL_PRICE)
                .entries(List.of(OrderEntryDto.builder()
                        .productType("ASTRO_MOUSE_TOYS")
                        .amount(2)
                        .build()))
                .build();

        Order result = orderService.placeOrder(CART_ID, CUSTOMER_REFERENCE, requestDto);

        assertEquals(CART_ID, result.getCartId());
        assertEquals(CUSTOMER_REFERENCE, result.getConsumerReference());
        assertEquals(TOTAL_PRICE, result.getTotalPrice());
        assertEquals(1, result.getEntries().size());
        assertEquals("ASTRO_MOUSE_TOYS", result.getEntries().get(0).getProductType().name());
        assertEquals(2, result.getEntries().get(0).getAmount());
    }
}

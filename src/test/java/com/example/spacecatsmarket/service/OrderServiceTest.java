package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.config.MappersTestConfiguration;
import com.example.spacecatsmarket.config.TestRestClientConfiguration;
import com.example.spacecatsmarket.config.WireMockConfig;
import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.dto.order.OrderEntryDto;
import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.service.impl.OrderServiceImpl;
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

@SpringBootTest(classes = {OrderServiceImpl.class})
@Import({MappersTestConfiguration.class, TestRestClientConfiguration.class, WireMockConfig.class})
@DisplayName("Order Service Tests")
@TestMethodOrder(OrderAnnotation.class)
class OrderServiceTest {

    @Autowired
    private OrderServiceImpl orderService;

    private static final UUID CART_ID = UUID.randomUUID();
    private static final String CUSTOMER_REFERENCE = "customerRef";
    private static final double TOTAL_PRICE = 105.0;

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

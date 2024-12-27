package com.example.spacecatsmarket.web;

import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.dto.order.OrderEntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Order Controller IT")
@Tag("order-service")
class OrderControllerIT {

    private static final UUID CART_ID = UUID.randomUUID();
    private static final String CUSTOMER_REFERENCE = "consumerReference";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PlaceOrderRequestDto placeOrderRequestDto;

    @BeforeEach
    void setUp() {
        placeOrderRequestDto = PlaceOrderRequestDto.builder()
                .totalPrice(50.0)
                .entries(List.of(
                        OrderEntryDto.builder()
                                .productType("ASTRO_MOUSE_TOYS")
                                .amount(3)
                                .build()
                ))
                .build();
    }

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/{customerReference}/orders/{cartId}", CUSTOMER_REFERENCE, CART_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeOrderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists());
    }
}

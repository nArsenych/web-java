package com.example.spacecatsmarket.web;

import com.example.spacecatsmarket.AbstractIt;
import com.example.spacecatsmarket.dto.whishlist.WishlistEntryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Wishlist Controller IT")
@Tag("wishlist-service")
class WishlistControllerIT extends AbstractIt {

    private static final UUID CUSTOMER_ID = UUID.fromString("fa535fe7-9f4b-4b5e-acb8-888b51cfe6dd");
    private static final UUID PRODUCT_ID = UUID.fromString("a60a023a-b2c5-4287-bf7d-8a9f217e6a58");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final WishlistEntryDto TEST_WISHLIST_ENTRY = WishlistEntryDto.builder()
            .customerId(CUSTOMER_ID)
            .productId(PRODUCT_ID)
            .notifiedWhenAvailable(true)
            .build();

    @Test
    void shouldAddToWishlist() throws Exception {
        mockMvc.perform(post("/api/v1/{customerId}/wishlist", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TEST_WISHLIST_ENTRY)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetWishlist() throws Exception {
        mockMvc.perform(get("/api/v1/{customerId}/wishlist", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    @Test
    void shouldRemoveFromWishlist() throws Exception {
        mockMvc.perform(delete("/api/v1/{customerId}/wishlist/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNoContent());
    }
}

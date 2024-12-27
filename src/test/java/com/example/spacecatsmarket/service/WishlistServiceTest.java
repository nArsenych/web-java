package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.domain.wishlist.WishlistEntry;
import com.example.spacecatsmarket.service.impl.WishlistServiceImpl;
import com.example.spacecatsmarket.service.interfaces.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WishlistServiceImpl.class)
@DisplayName("Wishlist Service Tests")
class WishlistServiceTest {

    @Autowired
    private WishlistService wishlistService;

    private static final UUID CUSTOMER_ID = UUID.fromString("b271d812-94bf-4931-9e7d-274fbfba65b3");
    private static final UUID PRODUCT_ID = UUID.fromString("a60a023a-b2c5-4287-bf7d-8a9f217e6a58");

    private static final WishlistEntry DEFAULT_ENTRY = WishlistEntry.builder()
            .customerId(CUSTOMER_ID)
            .productId(PRODUCT_ID)
            .notifiedWhenAvailable(true)
            .build();

    @Test
    void shouldAddToWishlist() {
        wishlistService.addToWishlist(CUSTOMER_ID, DEFAULT_ENTRY);

        List<WishlistEntry> wishlist = wishlistService.getWishlist(CUSTOMER_ID);

        assertEquals(1, wishlist.size());
        assertEquals(DEFAULT_ENTRY, wishlist.get(0));
    }

    @Test
    void shouldRemoveFromWishlist() {
        wishlistService.addToWishlist(CUSTOMER_ID, DEFAULT_ENTRY);
        wishlistService.removeFromWishlist(CUSTOMER_ID, DEFAULT_ENTRY.getProductId());

        List<WishlistEntry> wishlist = wishlistService.getWishlist(CUSTOMER_ID);

        assertTrue(wishlist.isEmpty());
    }

    @Test
    void shouldNotThrowExceptionWhenRemovingNonExistentProduct() {
        UUID customerId = UUID.randomUUID();
        UUID nonExistentProductId = UUID.randomUUID();

        assertDoesNotThrow(() -> wishlistService.removeFromWishlist(customerId, nonExistentProductId));
    }


    @Test
    void shouldReturnEmptyWishlistWhenNoEntriesExist() {
        List<WishlistEntry> wishlist = wishlistService.getWishlist(CUSTOMER_ID);

        assertTrue(wishlist.isEmpty());
    }
}

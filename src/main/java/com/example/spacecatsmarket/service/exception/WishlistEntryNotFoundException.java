package com.example.spacecatsmarket.service.exception;

import java.util.UUID;

public class WishlistEntryNotFoundException extends RuntimeException {

    private static final String WISHLIST_ENTRY_NOT_FOUND_MESSAGE = "Wishlist entry for customer '%s' and product '%s' not found";

    public WishlistEntryNotFoundException(UUID customerId, UUID productId) {
        super(String.format(WISHLIST_ENTRY_NOT_FOUND_MESSAGE, customerId, productId));
    }
}

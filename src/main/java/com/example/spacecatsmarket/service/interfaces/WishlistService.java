package com.example.spacecatsmarket.service.interfaces;

import com.example.spacecatsmarket.domain.wishlist.WishlistEntry;

import java.util.List;
import java.util.UUID;

public interface WishlistService {

    void addToWishlist(UUID customerId, WishlistEntry wishlistEntry);

    void removeFromWishlist(UUID customerId, UUID productId);

    List<WishlistEntry> getWishlist(UUID customerId);
}

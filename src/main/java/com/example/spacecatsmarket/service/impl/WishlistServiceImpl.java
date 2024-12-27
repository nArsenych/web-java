package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.wishlist.WishlistEntry;
import com.example.spacecatsmarket.service.interfaces.WishlistService;
import com.example.spacecatsmarket.service.exception.WishlistEntryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final Map<UUID, List<WishlistEntry>> customerWishlists = new HashMap<>();

    @Override
    public void addToWishlist(UUID customerId, WishlistEntry wishlistEntry) {
        customerWishlists.computeIfAbsent(customerId, k -> new ArrayList<>()).add(wishlistEntry);
        log.info("Product {} added to wishlist for customer {}", wishlistEntry.getProductId(), customerId);
    }

    @Override
    public void removeFromWishlist(UUID customerId, UUID productId) {
        List<WishlistEntry> wishlist = customerWishlists.get(customerId);
        if (wishlist == null) {
            return;
        }
        wishlist.removeIf(entry -> entry.getProductId().equals(productId));
    }

    @Override
    public List<WishlistEntry> getWishlist(UUID customerId) {
        return customerWishlists.getOrDefault(customerId, new ArrayList<>());
    }
}

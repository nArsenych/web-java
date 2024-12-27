package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.wishlist.WishlistEntry;
import com.example.spacecatsmarket.repository.CustomerRepository;
import com.example.spacecatsmarket.repository.ProductRepository;
import com.example.spacecatsmarket.repository.WishlistRepository;
import com.example.spacecatsmarket.repository.entity.WishlistEntryEntity;
import com.example.spacecatsmarket.service.exception.CustomerNotFoundException;
import com.example.spacecatsmarket.service.exception.ProductNotFoundException;
import com.example.spacecatsmarket.service.exception.WishlistEntryNotFoundException;
import com.example.spacecatsmarket.service.interfaces.WishlistService;
import com.example.spacecatsmarket.web.mapper.WishlistEntryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final WishlistEntryMapper wishlistEntryMapper;

    @Override
    public void addToWishlist(UUID customerId, WishlistEntry wishlistEntry) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        var product = productRepository.findById(wishlistEntry.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(wishlistEntry.getProductId()));

        WishlistEntryEntity entryEntity = new WishlistEntryEntity();
        entryEntity.setCustomer(customer);
        entryEntity.setProduct(product);
        entryEntity.setNotifiedWhenAvailable(wishlistEntry.isNotifiedWhenAvailable());

        wishlistRepository.save(entryEntity);
    }

    @Override
    public void removeFromWishlist(UUID customerId, UUID productId) {
        var entry = wishlistRepository.findAll().stream()
                .filter(e -> e.getCustomer().getId().equals(customerId) && e.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new WishlistEntryNotFoundException(customerId, productId));

        wishlistRepository.delete(entry);
    }

    @Override
    public List<WishlistEntry> getWishlist(UUID customerId) {
        return wishlistRepository.findAll().stream()
                .filter(entry -> entry.getCustomer().getId().equals(customerId))
                .map(wishlistEntryMapper::toWishlistEntry)
                .collect(Collectors.toList());
    }
}

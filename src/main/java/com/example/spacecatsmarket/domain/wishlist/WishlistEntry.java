package com.example.spacecatsmarket.domain.wishlist;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class WishlistEntry {

    UUID customerId;
    UUID productId;
    boolean notifiedWhenAvailable;
}

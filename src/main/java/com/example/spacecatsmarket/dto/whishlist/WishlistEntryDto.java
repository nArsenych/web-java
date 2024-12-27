package com.example.spacecatsmarket.dto.whishlist;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class WishlistEntryDto {

    UUID customerId;

    @NotNull(message = "Product ID cannot be null")
    UUID productId;

    @NotNull(message = "Notified flag cannot be null")
    boolean notifiedWhenAvailable;
}

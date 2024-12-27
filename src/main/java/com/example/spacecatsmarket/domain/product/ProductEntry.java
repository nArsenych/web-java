package com.example.spacecatsmarket.domain.product;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ProductEntry {

    UUID productId;
    int quantity;
}

package com.example.spacecatsmarket.domain.product;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Product {

    UUID id;
    String name;
    String description;
    Double price;
    String category;
    int stock;
}

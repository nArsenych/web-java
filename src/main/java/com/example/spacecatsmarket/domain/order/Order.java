package com.example.spacecatsmarket.domain.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Order {

    UUID id;
    List<OrderEntry> entries;
    UUID cartId;
    String consumerReference;
    Double totalPrice;


}

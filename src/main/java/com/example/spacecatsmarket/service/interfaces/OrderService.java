package com.example.spacecatsmarket.service.interfaces;

import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;

import java.util.UUID;

public interface OrderService {

    Order placeOrder(UUID cartId, String customerReference, PlaceOrderRequestDto requestDto);
}

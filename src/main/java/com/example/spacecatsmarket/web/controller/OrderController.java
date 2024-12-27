package com.example.spacecatsmarket.web.controller;

import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.dto.order.PlaceOrderResponseDto;
import com.example.spacecatsmarket.service.interfaces.OrderService;
import com.example.spacecatsmarket.web.mapper.OrderDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/{customerReference}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping("/{cartId}")
    public ResponseEntity<PlaceOrderResponseDto> placeOrder(
            @PathVariable("customerReference") String customerReference,
            @PathVariable("cartId") UUID cartId,
            @RequestBody @Valid PlaceOrderRequestDto placeOrderDto) {

        var order = orderService.placeOrder(cartId, customerReference, placeOrderDto);
        return ResponseEntity.ok(orderDtoMapper.toPlaceOrderResponseDto(order));
    }
}

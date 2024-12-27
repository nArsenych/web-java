package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.common.ProductType;
import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.domain.order.OrderEntry;
import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.dto.order.OrderAuditResponseDto;
import com.example.spacecatsmarket.service.interfaces.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final RestClient orderAuditClient;
    private final String orderAuditServiceEndpoint;

    public OrderServiceImpl(@Qualifier("orderAuditRestClient") RestClient orderAuditClient,
                            @Value("${application.order-audit-service.orders}") String orderAuditServiceEndpoint) {
        this.orderAuditClient = orderAuditClient;
        this.orderAuditServiceEndpoint = orderAuditServiceEndpoint;
    }

    @Override
    public Order placeOrder(UUID cartId, String customerReference, PlaceOrderRequestDto requestDto) {
        log.info("Creating order for cart: {}, customer: {}", cartId, customerReference);

        List<OrderEntry> entries = requestDto.getEntries().stream()
                .map(entryDto -> OrderEntry.builder()
                        .productType(ProductType.valueOf(entryDto.getProductType()))
                        .amount(entryDto.getAmount())
                        .build())
                .toList();

        Order order = createOrder(cartId, entries, requestDto.getTotalPrice(), customerReference);

        OrderAuditResponseDto response = sendOrderToAudit(order);

        log.info("Order audit response: {}", response.getStatus());

        return order;
    }

    private Order createOrder(UUID cartId, List<OrderEntry> entries, Double totalPrice, String customerReference) {
        return Order.builder()
                .id(UUID.randomUUID())
                .cartId(cartId)
                .entries(entries)
                .totalPrice(totalPrice)
                .consumerReference(customerReference)
                .build();
    }

    private OrderAuditResponseDto sendOrderToAudit(Order order) {
        log.info("Sending order {} to audit service", order.getId());

        return orderAuditClient.post()
                .uri(orderAuditServiceEndpoint)
                .body(order)
                .contentType(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    log.error("Order audit service response failed. Response Code {}", response.getStatusCode());
                    throw new RuntimeException("Order audit service failed");
                })
                .body(OrderAuditResponseDto.class);
    }
}

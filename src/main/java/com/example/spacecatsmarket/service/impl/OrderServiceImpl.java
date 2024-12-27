package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.common.ProductType;
import com.example.spacecatsmarket.domain.order.OrderEntry;
import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.repository.OrderRepository;
import com.example.spacecatsmarket.repository.ProductRepository;
import com.example.spacecatsmarket.repository.entity.OrderEntity;
import com.example.spacecatsmarket.repository.entity.OrderEntryEntity;
import com.example.spacecatsmarket.repository.entity.ProductEntity;
import com.example.spacecatsmarket.service.interfaces.OrderService;
import com.example.spacecatsmarket.web.mapper.OrderDtoMapper;
import com.example.spacecatsmarket.web.mapper.OrderEntryMapper;
import com.example.spacecatsmarket.dto.order.OrderAuditResponseDto;
import com.example.spacecatsmarket.domain.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final RestClient orderAuditClient;
    private final String orderAuditServiceEndpoint;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEntryMapper orderEntryMapper;
    private final OrderDtoMapper orderDtoMapper;

    public OrderServiceImpl(@Qualifier("orderAuditRestClient") RestClient orderAuditClient,
                            @Value("${application.order-audit-service.orders}") String orderAuditServiceEndpoint,
                            OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderEntryMapper orderEntryMapper,
                            OrderDtoMapper orderDtoMapper) {
        this.orderAuditClient = orderAuditClient;
        this.orderAuditServiceEndpoint = orderAuditServiceEndpoint;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderEntryMapper = orderEntryMapper;
        this.orderDtoMapper = orderDtoMapper;
    }

    @Override
    public Order placeOrder(UUID cartId, String customerReference, PlaceOrderRequestDto requestDto) {
        log.info("Creating order for cart: {}, customer: {}", cartId, customerReference);

        List<OrderEntryEntity> entries = requestDto.getEntries().stream()
                .map(entryDto -> {
                    ProductEntity productEntity = productRepository.findByName(entryDto.getProductType())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entryDto.getProductType()));

                    OrderEntryEntity orderEntryEntity = orderEntryMapper.toOrderEntryEntity(OrderEntry.builder()
                            .productType(ProductType.valueOf(entryDto.getProductType()))
                            .amount(entryDto.getAmount())
                            .build());

                    orderEntryEntity.setProduct(productEntity);
                    return orderEntryEntity;
                })
                .collect(Collectors.toList());

        final OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(UUID.randomUUID());
        orderEntity.setCartId(cartId);
        orderEntity.setConsumerReference(customerReference);
        orderEntity.setTotalPrice(requestDto.getTotalPrice());
        orderEntity.setEntries(entries);

        entries.forEach(entry -> entry.setOrder(orderEntity));

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

        OrderAuditResponseDto response = sendOrderToAudit(savedOrderEntity);
        log.info("Order audit response: {}", response.getStatus());

        return orderDtoMapper.toOrder(savedOrderEntity);
    }


    private OrderAuditResponseDto sendOrderToAudit(OrderEntity orderEntity) {
        log.info("Sending order {} to audit service", orderEntity.getId());

        return orderAuditClient.post()
                .uri(orderAuditServiceEndpoint)
                .body(orderEntity)
                .contentType(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    log.error("Order audit service response failed. Response Code {}", response.getStatusCode());
                    throw new RuntimeException("Order audit service failed");
                })
                .body(OrderAuditResponseDto.class);
    }
}

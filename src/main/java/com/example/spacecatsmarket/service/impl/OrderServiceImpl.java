package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.domain.order.OrderContext;
import com.example.spacecatsmarket.domain.order.OrderEntry;
import com.example.spacecatsmarket.domain.payment.PaymentTransaction;
import com.example.spacecatsmarket.repository.OrderRepository;
import com.example.spacecatsmarket.repository.entity.OrderEntity;
import com.example.spacecatsmarket.repository.entity.OrderEntryEntity;
import com.example.spacecatsmarket.service.interfaces.OrderService;
import com.example.spacecatsmarket.service.interfaces.PaymentService;
import com.example.spacecatsmarket.service.exception.PaymentTransactionFailed;
import com.example.spacecatsmarket.web.mapper.OrderEntryMapper;
import com.example.spacecatsmarket.web.mapper.PaymentServiceMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.spacecatsmarket.common.PaymentStatus.FAILURE;

@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final PaymentService paymentService;
    private final PaymentServiceMapper paymentMapper;
    private final OrderEntryMapper orderEntryMapper;
    private final OrderRepository orderRepository;


    public OrderServiceImpl(PaymentService paymentService, PaymentServiceMapper paymentMapper, OrderEntryMapper orderEntryMapper, OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
        this.orderEntryMapper = orderEntryMapper;
        this.orderRepository = orderRepository;
    }


    @Override
    public Order placeOrder(OrderContext orderContext) {
        log.info("Placing order for cart with id: {}", orderContext.getCartId());
        PaymentTransaction paymentTransaction = paymentService.processPayment(paymentMapper.toPayment(orderContext));
        if (FAILURE.equals(paymentTransaction.getStatus())) {
            throw new PaymentTransactionFailed(paymentTransaction.getId(), orderContext.getCartId());
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(null);
        orderEntity.setTransactionId(paymentTransaction.getId());
        orderEntity.setCartId(orderContext.getCartId());
        orderEntity.setConsumerReference(orderContext.getCustomerReference());
        orderEntity.setTotalPrice(orderContext.getTotalPrice());

        List<OrderEntryEntity> entryEntities = orderContext.getEntries().stream()
                .map(orderEntryMapper::toOrderEntryEntity)
                .peek(entry -> entry.setOrder(orderEntity))
                .collect(Collectors.toList());

        orderEntity.setEntries(entryEntities);
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        return createOrderMock(savedOrder);
    }

    private Order createOrderMock(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .cartId(entity.getCartId())
                .consumerReference(entity.getConsumerReference())
                .totalPrice(entity.getTotalPrice())
                .entries(entity.getEntries().stream()
                        .map(orderEntryMapper::toOrderEntry)
                        .collect(Collectors.toList()))
                .build();
    }
}

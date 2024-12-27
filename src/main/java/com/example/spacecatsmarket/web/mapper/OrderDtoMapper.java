package com.example.spacecatsmarket.web.mapper;

import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.repository.entity.OrderEntity;
import com.example.spacecatsmarket.dto.order.PlaceOrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderEntryMapper.class)
public interface OrderDtoMapper {

    @Mapping(target = "orderId", source = "id")
    PlaceOrderResponseDto toPlaceOrderResponseDto(Order order);

    Order toOrder(OrderEntity entity);

    OrderEntity toOrderEntity(Order order);
}

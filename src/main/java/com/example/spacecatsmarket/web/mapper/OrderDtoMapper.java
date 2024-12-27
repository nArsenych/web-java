package com.example.spacecatsmarket.web.mapper;

import com.example.spacecatsmarket.domain.order.Order;
import com.example.spacecatsmarket.dto.order.PlaceOrderRequestDto;
import com.example.spacecatsmarket.dto.order.PlaceOrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    @Mapping(target = "orderId", source = "id")
    PlaceOrderResponseDto toPlaceOrderResponseDto(Order order);
}

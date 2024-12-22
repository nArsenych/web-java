package com.example.spacecatsmarket.web.mapper;

import com.example.spacecatsmarket.common.ProductType;
import com.example.spacecatsmarket.domain.order.OrderEntry;
import com.example.spacecatsmarket.repository.entity.OrderEntryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = ProductType.class)
public interface OrderEntryMapper {

    @Mapping(target = "productType", source = "productType")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "productId", source = "product.id")
    OrderEntry toOrderEntry(OrderEntryEntity entity);

    @Mapping(target = "productType", source = "productType")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "product.id", source = "productId")
    OrderEntryEntity toOrderEntryEntity(OrderEntry entry);
}

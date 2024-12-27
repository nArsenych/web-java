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
    OrderEntryEntity toOrderEntryEntity(OrderEntry entry);

    @Mapping(target = "productType", source = "productType")
    @Mapping(target = "amount", source = "amount")
    OrderEntry toOrderEntry(OrderEntryEntity entity);
}

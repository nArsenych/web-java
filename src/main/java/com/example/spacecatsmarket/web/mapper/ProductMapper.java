package com.example.spacecatsmarket.web.mapper;

import com.example.spacecatsmarket.domain.product.Product;
import com.example.spacecatsmarket.dto.product.ProductDto;
import com.example.spacecatsmarket.repository.entity.CategoryEntity;
import com.example.spacecatsmarket.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "description", source = "product.description")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "stock", source = "product.stock")
    @Mapping(target = "category", source = "product.category")
    ProductDto toProductDto(Product product);

    @Mapping(target = "id", source = "productDto.id")
    @Mapping(target = "name", source = "productDto.name")
    @Mapping(target = "description", source = "productDto.description")
    @Mapping(target = "price", source = "productDto.price")
    @Mapping(target = "stock", source = "productDto.stock")
    @Mapping(target = "category", source = "productDto.category")
    Product toProduct(ProductDto productDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryEntityToString")
    Product toProduct(ProductEntity productEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "category", source = "category", qualifiedByName = "stringToCategoryEntity")
    ProductEntity toProductEntity(Product product);

    @Named("categoryEntityToString")
    default String categoryEntityToString(CategoryEntity categoryEntity) {
        return categoryEntity != null ? categoryEntity.getName() : null;
    }

    @Named("stringToCategoryEntity")
    default CategoryEntity stringToCategoryEntity(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryName);
        return categoryEntity;
    }
}

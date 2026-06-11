package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return ProductDto.builder().id(product.getId()).name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).discountedPrice(product.getDiscountedPrice()).brand(product.getBrand()).imageUrl(product.getImageUrl()).categoryId(
                        product.getCategory() != null ? product.getCategory().getId() : null).build();
    }
    public Product toEntity(ProductDto dto, Category category) {
        return Product.builder().name(dto.getName()).description(dto.getDescription()).price(dto.getPrice())
                .discountedPrice(dto.getDiscountedPrice()).brand(dto.getBrand()).imageUrl(dto.getImageUrl())
                .category(category).build();
    }
}

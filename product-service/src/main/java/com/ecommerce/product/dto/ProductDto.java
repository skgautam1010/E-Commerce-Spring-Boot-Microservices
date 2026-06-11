package com.ecommerce.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product Name is required")
    private String name;
    @NotBlank
    private String description;
    @Positive(message = "Price must be positive")
    private double price;
    private double discountedPrice;
    private String brand;
    private String imageUrl;
    @NotNull(message = "Category Id is required")
    private Long categoryId;
}

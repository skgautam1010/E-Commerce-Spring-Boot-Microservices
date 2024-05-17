package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Category Name cannot be Empty")
    private String name;
    private String description;
    private Long parentId;
}

package com.ecommerce.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequestDto {
    @NotBlank(message = "SKU Code must not be blank")
    private String skuCode;
    @Min(value = 0 , message = "Quantity must be 0 or more")
    private Integer quantity;
}

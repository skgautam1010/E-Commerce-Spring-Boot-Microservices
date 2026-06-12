package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryReservationRequest {
    @NotNull
    private String skuCode;
    @Min(value = 1 , message = "Quantity must be 0 or more")
    private Integer quantity;
    private String orderNumber;
}

package com.ecommerce.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "Price is Mandatory")
    private BigDecimal price;
    @NotNull
    private String skuCode;
    @Min(value=1, message = "Quantity Must be atleast 1")
    private Integer quantity;
}

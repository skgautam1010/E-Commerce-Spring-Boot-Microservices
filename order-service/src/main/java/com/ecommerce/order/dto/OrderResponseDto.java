package com.ecommerce.order.dto;

import com.ecommerce.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
}

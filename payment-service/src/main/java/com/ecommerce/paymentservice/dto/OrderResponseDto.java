package com.ecommerce.paymentservice.dto;

import com.ecommerce.paymentservice.enums.OrderStatus;
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
    private Long userId;
}

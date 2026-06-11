package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDto dto) {
        Order order = new Order();
        order.setSkuCode(dto.getSkuCode());
        order.setQuantity(dto.getQuantity());
        return order;
    }

    public OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(order.getOrderNumber(), order.getOrderStatus(), order.getTotalAmount());
    }
}

package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto dto, Long userId);
    OrderResponseDto getOrderByOrderNumber(String orderNumber);
    void confirmOrder(String orderNumber);
    void failOrder(String orderNumber);

}

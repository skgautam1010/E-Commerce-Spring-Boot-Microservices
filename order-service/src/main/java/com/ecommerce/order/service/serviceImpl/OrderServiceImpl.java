package com.ecommerce.order.service.serviceImpl;

import com.ecommerce.order.client.InventoryFeignClient;
import com.ecommerce.order.dto.InventoryResponseDto;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto) {
        InventoryResponseDto inventoryResponseDto = inventoryFeignClient.isInStock(dto.getSkuCode());
        if(!inventoryResponseDto.isInStock()) {
            throw new RuntimeException("Product is out of Stock");
        }
        Order order = orderMapper.toEntity(dto);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}

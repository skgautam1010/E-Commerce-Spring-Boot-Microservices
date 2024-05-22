package com.ecommerce.order.service.serviceImpl;

import com.ecommerce.order.client.InventoryFeignClient;
import com.ecommerce.order.dto.InventoryResponseDto;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.event.OrderPlacedEvent;
import com.ecommerce.order.kafka.OrderEventProducer;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;
    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto) {
        InventoryResponseDto inventoryResponseDto = inventoryFeignClient.isInStock(dto.getSkuCode());
        if(!inventoryResponseDto.isInStock()) {
            throw new RuntimeException("Product is out of Stock");
        }
        Order order = orderMapper.toEntity(dto);
        String orderId = UUID.randomUUID().toString();
        order.setOrderNumber(orderId);
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId);
        event.setSkuCode(dto.getSkuCode());
        event.setQuantity(dto.getQuantity());
        event.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(event);
        return orderMapper.toDto(order);
    }
}

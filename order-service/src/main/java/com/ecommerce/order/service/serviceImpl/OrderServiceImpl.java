package com.ecommerce.order.service.serviceImpl;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.client.InventoryFeignClient;
import com.ecommerce.order.client.ProductFeignClient;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.event.OrderPlacedEvent;
import com.ecommerce.order.kafka.OrderEventProducer;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;
    private final OrderEventProducer orderEventProducer;
    private final ProductFeignClient productFeignClient;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto dto) {
        InventoryResponseDto inventoryResponseDto = inventoryFeignClient.isInStock(dto.getSkuCode());
        if(inventoryResponseDto.getAvailableQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Product is out of Stock");
        }
        ProductResponseDto productResponseDto = productFeignClient.getProductsById(inventoryResponseDto.getProductId());
        if(productResponseDto == null) {
            throw new RuntimeException("Product not found for productId : " + inventoryResponseDto.getProductId());
        }

        BigDecimal unitPrice;
        if(productResponseDto.getDiscountedPrice() > 0) {
            unitPrice = BigDecimal.valueOf(productResponseDto.getDiscountedPrice());
        } else {
            unitPrice = BigDecimal.valueOf(productResponseDto.getPrice());
        }
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));
        if (dto.getExpectedAmount().compareTo(totalAmount) != 0) {
            throw new RuntimeException("Amount mismatch");
        }

        String orderNumber = UUID.randomUUID().toString();
        InventoryReservationRequest reservationRequest = InventoryReservationRequest.builder()
                .orderNumber(orderNumber).skuCode(dto.getSkuCode()).quantity(dto.getQuantity()).build();

        inventoryFeignClient.reserveStock(reservationRequest);

        Order order = orderMapper.toEntity(dto);

        order.setOrderNumber(orderNumber);
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(totalAmount);
        order.setProductId(inventoryResponseDto.getProductId());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public void confirmOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new RuntimeException("Order Not Found"));
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);

        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderNumber(order.getOrderNumber());
        event.setSkuCode(order.getSkuCode());
        event.setQuantity(order.getQuantity());
        event.setEventType("PAID");
        event.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(event);
    }

    @Override
    public void failOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        order.setOrderStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderNumber(order.getOrderNumber());
        event.setSkuCode(order.getSkuCode());
        event.setQuantity(order.getQuantity());
        event.setEventType("FAILED");
        event.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(event);
    }
}

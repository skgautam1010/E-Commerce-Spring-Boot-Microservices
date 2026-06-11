package com.ecommerce.order.service.serviceImpl;

import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.client.InventoryFeignClient;
import com.ecommerce.order.client.ProductFeignClient;
import com.ecommerce.order.dto.InventoryResponseDto;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.ProductResponseDto;
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
        Order order = orderMapper.toEntity(dto);
        String orderId = UUID.randomUUID().toString();
        order.setOrderNumber(orderId);
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(totalAmount);
        order.setProductId(productResponseDto.getId());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(orderId);
        event.setSkuCode(dto.getSkuCode());
        event.setQuantity(dto.getQuantity());
        event.setEventTime(LocalDateTime.now());
        //orderEventProducer.sendOrderEvent(event);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public void confirmOrder(String orderNumber) {

    }

    @Override
    public void failOrder(String orderNumber) {

    }
}

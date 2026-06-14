package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestHeader("X-User-Id") Long userId, @RequestBody OrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(dto, userId));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    @PostMapping("/{orderNumber}/confirm")
    public ResponseEntity<String> confirmOrder(@PathVariable String orderNumber) {
        orderService.confirmOrder(orderNumber);
        return ResponseEntity.ok("Order Is Placed");
    }

    @PostMapping("/{orderNumber}/fail")
    public ResponseEntity<String> failOrder(@PathVariable String orderNumber) {
        orderService.failOrder(orderNumber);
        return ResponseEntity.ok("Order Failed");
    }
}

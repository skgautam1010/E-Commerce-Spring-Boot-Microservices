package com.ecommerce.paymentservice.client;

import com.ecommerce.paymentservice.dto.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "order-service")
public interface OrderClient {
    @PostMapping("/api/orders/{orderNumber}/confirm")
    void confirmOrder(@PathVariable String orderNumber);

    @PostMapping("/api/orders/{orderNumber}/fail")
    void failOrder(@PathVariable String orderNumber);

    @GetMapping("/api/orders/{orderNumber}")
    OrderResponseDto getOrder(@PathVariable String orderNumber);
}

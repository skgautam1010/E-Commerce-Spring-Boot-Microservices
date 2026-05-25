package com.ecommerce.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "order-service")
public interface OrderClient {
    @PostMapping("/api/orders/{orderId}/confirm")
    void confirmOrder(@PathVariable String orderId);

    @PostMapping("/api/orders/{orderId}/fail")
    void failedOrder(@PathVariable String orderId);
}

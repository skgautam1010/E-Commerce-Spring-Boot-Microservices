package com.ecommerce.order.client;

import com.ecommerce.order.dto.InventoryReservationRequest;
import com.ecommerce.order.dto.InventoryResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

    @GetMapping("/api/inventory/{skuCode}")
    InventoryResponseDto isInStock(@PathVariable String skuCode);

    @PostMapping("/api/inventory/reserve")
    void reserveStock(@RequestBody @Valid InventoryReservationRequest request);
}

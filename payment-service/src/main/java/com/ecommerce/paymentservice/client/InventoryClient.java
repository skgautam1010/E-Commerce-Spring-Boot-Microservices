package com.ecommerce.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping("/api/inventory/{orderNumber}/commit")
    void commitInventory(@PathVariable String orderNumber);

     @PostMapping("/api/inventory/{orderNumber}/rollback")
    void rollbackInventory(@PathVariable String orderNumber);
}

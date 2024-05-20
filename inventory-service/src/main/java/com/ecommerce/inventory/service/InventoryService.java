package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryResponseDto;

public interface InventoryService {
    InventoryResponseDto checkInventory(String skuCode);
    void addInventory(InventoryRequestDto dto);
}

package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryReservationRequest;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.event.OrderPlacedEvent;

public interface InventoryService {
    InventoryResponseDto checkInventory(String skuCode);
    void addInventory(InventoryRequestDto dto);
    void reserveStock(InventoryReservationRequest request);
    void confirmReservation(InventoryReservationRequest request);
    void releaseReservation(InventoryReservationRequest request);
}

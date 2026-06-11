package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public Inventory toEntity(InventoryRequestDto dto) {
        return Inventory.builder().skuCode(dto.getSkuCode()).quantity(dto.getQuantity()).productId(dto.getProductId()).build();
    }
    public InventoryResponseDto toDto(Inventory inventory) {
        return InventoryResponseDto.builder().productId(inventory.getProductId()).skuCode(inventory.getSkuCode()).inStock(inventory.getQuantity() > 0).availableQuantity(inventory.getQuantity()).build();
    }
}

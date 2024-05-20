package com.ecommerce.inventory.service.serviceImpl;



import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exceptions.InventoryException;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponseDto checkInventory(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode).orElseThrow(() -> new InventoryException(skuCode));
        return inventoryMapper.toDto(inventory);
    }

    @Override
    public void addInventory(InventoryRequestDto dto) {
        Inventory inventory = inventoryMapper.toEntity(dto);
        inventoryRepository.save(inventory);
    }
}
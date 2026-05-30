package com.ecommerce.inventory.service.serviceImpl;



import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.entity.ProcessedOrder;
import com.ecommerce.inventory.event.OrderPlacedEvent;
import com.ecommerce.inventory.exceptions.InventoryException;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.repository.ProcessedOrderRepository;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProcessedOrderRepository processedOrderRepository;
    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

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

    @Transactional
    @Override
    public void updateInventory(OrderPlacedEvent event) {
        if(processedOrderRepository.existsByOrderId(event.getOrderId())) {
            log.info("Already Processed this order: {}", event.getOrderId());
            return;
        }
        Inventory inventory =  inventoryRepository.findBySkuCode(event.getSkuCode()).orElseThrow(() -> new InventoryException("Not Found" + event.getSkuCode()));
        Integer availableQuantity = inventory.getQuantity();
        Integer orderQuantity = event.getQuantity();
        if(availableQuantity < orderQuantity) {
            throw new RuntimeException("Insufficient Stock for SKU Code");
        }
        inventory.setQuantity(availableQuantity - orderQuantity);
        inventoryRepository.save(inventory);
        ProcessedOrder processedOrder = new ProcessedOrder();
        processedOrder.setOrderId(event.getOrderId());
        processedOrder.setProcessedAt(LocalDateTime.now());
        processedOrderRepository.save(processedOrder);
    }
}
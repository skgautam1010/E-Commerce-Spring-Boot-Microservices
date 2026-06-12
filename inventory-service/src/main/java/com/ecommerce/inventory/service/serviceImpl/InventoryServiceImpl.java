package com.ecommerce.inventory.service.serviceImpl;



import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryReservationRequest;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exceptions.InventoryException;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
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

    @Override
    @Transactional
    public void reserveStock(InventoryReservationRequest reservationRequest) {
        Inventory inventory = inventoryRepository.findBySkuCodeForUpdate(reservationRequest.getSkuCode()).orElseThrow(() -> new InventoryException("Inventory not found for sku : " + reservationRequest.getSkuCode()));
        int availableQuantity = inventory.getQuantity() - inventory.getReservedQuantity();
        if(availableQuantity < reservationRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() + reservationRequest.getQuantity());
    }

    @Override
    public void confirmReservation(InventoryReservationRequest request) {
        Inventory inventory = inventoryRepository.findBySkuCodeForUpdate(request.getSkuCode()).orElseThrow(() ->
                        new InventoryException(
                               "Inventory Not Found: " +  request.getSkuCode()
                        ));

        if(inventory.getReservedQuantity() < request.getQuantity()) {
            throw new RuntimeException("Reserved quantity is insufficient");
        }

        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);
    }

    @Override
    public void releaseReservation(InventoryReservationRequest request) {

        Inventory inventory = inventoryRepository
                .findBySkuCodeForUpdate(request.getSkuCode())
                .orElseThrow(() ->
                        new InventoryException(
                               "Inventory Not Found: " + request.getSkuCode()
                        ));

        if(inventory.getReservedQuantity() < request.getQuantity()) {
            throw new RuntimeException("Reserved quantity is insufficient");
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);
    }

}
package com.ecommerce.inventory.kafka;

import com.ecommerce.inventory.dto.InventoryReservationRequest;
import com.ecommerce.inventory.entity.ProcessedOrder;
import com.ecommerce.inventory.event.OrderPlacedEvent;
import com.ecommerce.inventory.repository.ProcessedOrderRepository;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final InventoryService inventoryService;
    private final ProcessedOrderRepository processedOrderRepository;

    @KafkaListener(topics = "order-inventory-events", groupId = "inventory-group")
    @Transactional
    public void consume(OrderPlacedEvent event) {

        if(processedOrderRepository.existsByOrderNumber(event.getOrderNumber())) {
            log.info("Order already processed : {}", event.getOrderNumber());
            return;
        }

        InventoryReservationRequest inventoryReservationRequest = new InventoryReservationRequest();
        inventoryReservationRequest.setOrderNumber(event.getOrderNumber());
        inventoryReservationRequest.setSkuCode(event.getSkuCode());
        inventoryReservationRequest.setQuantity(event.getQuantity());

        if("PAID".equalsIgnoreCase(event.getEventType())) {
            inventoryService.confirmReservation(inventoryReservationRequest);
        } else if("FAILED".equalsIgnoreCase(event.getEventType())) {
            inventoryService.releaseReservation(inventoryReservationRequest);
        }

        ProcessedOrder processedOrder = new ProcessedOrder();
        processedOrder.setOrderNumber(event.getOrderNumber());
        processedOrder.setProcessedAt(LocalDateTime.now());

        processedOrderRepository.save(processedOrder);
    }


}

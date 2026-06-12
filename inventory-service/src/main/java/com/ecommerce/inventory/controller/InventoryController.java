package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryReservationRequest;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/create")
    public ResponseEntity<String> createInventory(@RequestBody @Valid InventoryRequestDto dto) {
        inventoryService.addInventory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory Created");
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponseDto> isInStock(@PathVariable String skuCode) {
        return ResponseEntity.ok(inventoryService.checkInventory(skuCode));
    }
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveInventory(@RequestBody @Valid InventoryReservationRequest reservationRequest) {
        inventoryService.reserveStock(reservationRequest);
        return ResponseEntity.ok("Stock Reserved Successfully");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmReservation(@RequestBody @Valid InventoryReservationRequest request) {
        inventoryService.confirmReservation(request);
        return ResponseEntity.ok("Inventory Reservation Confirmed");
    }

    @PostMapping("/release")
    public ResponseEntity<String> releaseReservation(@RequestBody InventoryReservationRequest request) {
        inventoryService.releaseReservation(request);
        return ResponseEntity.ok("Inventory Reservation Released");
    }

}

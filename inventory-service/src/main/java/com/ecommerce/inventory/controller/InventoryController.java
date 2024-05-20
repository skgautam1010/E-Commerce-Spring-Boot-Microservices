package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequestDto;
import com.ecommerce.inventory.dto.InventoryResponseDto;
import com.ecommerce.inventory.service.InventoryService;
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
    public ResponseEntity<String> createInventory(@RequestBody InventoryRequestDto dto) {
        inventoryService.addInventory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Inventory Created");
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponseDto> isInStock(@PathVariable String skuCode) {
        return ResponseEntity.ok(inventoryService.checkInventory(skuCode));
    }
}

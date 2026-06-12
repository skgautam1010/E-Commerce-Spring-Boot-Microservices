package com.ecommerce.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
    private String eventId;
    private String orderNumber;
    private String skuCode;
    private Integer quantity;
    private String eventType;
    private LocalDateTime eventTime;
}

package com.ecommerce.order.kafka;

import com.ecommerce.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderEvent(OrderPlacedEvent event) {
        kafkaTemplate.send("order-inventory-events", event.getOrderNumber(), event);
    }
}

package com.ecommerce.paymentservice.kafka;

import com.ecommerce.paymentservice.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(NotificationEvent event) {
        kafkaTemplate.send("notification-topic", event.getOrderNumber(), event);
    }
}

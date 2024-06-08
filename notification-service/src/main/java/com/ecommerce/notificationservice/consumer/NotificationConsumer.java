package com.ecommerce.notificationservice.consumer;

import com.ecommerce.notificationservice.dto.NotificationEvent;
import com.ecommerce.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        emailService.sendMail(event.getEmail(), event.getSubject(), event.getMessage());
    }
}

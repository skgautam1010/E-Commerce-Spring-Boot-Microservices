package com.ecommerce.notificationservice.consumer;

import com.ecommerce.notificationservice.dto.NotificationEvent;
import com.ecommerce.notificationservice.service.EmailService;
import com.ecommerce.notificationservice.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;
    private final SmsService smsService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        if("email".equalsIgnoreCase(event.getType())) {
            emailService.sendMail(event.getEmail(), event.getSubject(), event.getMessage());
        }
        if("sms".equalsIgnoreCase(event.getType())) {
            smsService.sendSms(event.getMobile(), event.getMessage());
        }
    }
}

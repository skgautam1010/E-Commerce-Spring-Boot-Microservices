package com.ecommerce.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String eventId;
    private String email;
    private String mobile;
    private String type;
    private String subject;
    private String message;
    private String orderNumber;
    private BigDecimal amount;
    private LocalDateTime eventTime;
}

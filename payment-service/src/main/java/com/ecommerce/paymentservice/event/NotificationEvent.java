package com.ecommerce.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

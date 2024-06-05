package com.ecommerce.paymentservice.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class GatewayOrderResponse {
    private String gatewayOrderId;
    private String currency;
    private BigDecimal amount;
    private String status;
    private String gatewayName;
}

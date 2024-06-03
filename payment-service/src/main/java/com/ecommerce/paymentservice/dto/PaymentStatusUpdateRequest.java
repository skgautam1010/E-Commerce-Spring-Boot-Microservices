package com.ecommerce.paymentservice.dto;

import com.ecommerce.paymentservice.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public class PaymentStatusUpdateRequest {
    @NotNull
    private PaymentStatus paymentStatus;
}

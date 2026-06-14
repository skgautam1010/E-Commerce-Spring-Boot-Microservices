package com.ecommerce.paymentservice.mapper;

import com.ecommerce.paymentservice.dto.PaymentRequestDto;
import com.ecommerce.paymentservice.dto.PaymentResponseDto;
import com.ecommerce.paymentservice.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDto dto) {
        return Payment.builder().orderNumber(dto.getOrderNumber()).paymentMethod(dto.getPaymentMethod()).build();
    }

    public PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder().orderNumber(payment.getOrderNumber()).paymentId(payment.getId()).amount(payment.getAmount())
                .gatewayOrderId(payment.getGatewayOrderId()).paymentStatus(payment.getPaymentStatus()).build();
    }
}

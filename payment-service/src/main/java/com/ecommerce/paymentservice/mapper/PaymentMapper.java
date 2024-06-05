package com.ecommerce.paymentservice.mapper;

import com.ecommerce.paymentservice.dto.PaymentRequestDto;
import com.ecommerce.paymentservice.dto.PaymentResponseDto;
import com.ecommerce.paymentservice.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDto dto) {
        return Payment.builder().orderId(dto.getOrderId()).amount(dto.getAmount()).paymentMethod(dto.getPaymentMethod()).build();
    }

    public PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder().orderId(payment.getOrderId()).paymentId(payment.getId()).amount(payment.getAmount())
                .transactionId(payment.getTransactionId()).paymentStatus(payment.getPaymentStatus()).build();
    }
}

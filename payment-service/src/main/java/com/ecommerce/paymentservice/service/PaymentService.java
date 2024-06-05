package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentRequestDto;
import com.ecommerce.paymentservice.dto.PaymentResponseDto;
import com.ecommerce.paymentservice.dto.PaymentStatusUpdateRequest;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto getPaymentByOrderId(String orderId);
    PaymentResponseDto updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequest statusUpdateRequest);
}

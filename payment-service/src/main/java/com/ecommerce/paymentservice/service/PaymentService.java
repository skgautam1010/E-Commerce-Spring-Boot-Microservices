package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentRequestDto;
import com.ecommerce.paymentservice.dto.PaymentResponseDto;
import com.ecommerce.paymentservice.dto.PaymentStatusUpdateRequest;
import com.razorpay.RazorpayException;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) throws RazorpayException;
    PaymentResponseDto getPaymentByOrderNumber(String orderNumber);
    PaymentResponseDto updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequest statusUpdateRequest);
    PaymentResponseDto markPaymentSuccess(Long paymentId);
    PaymentResponseDto markPaymentFailure(Long paymentId);
}

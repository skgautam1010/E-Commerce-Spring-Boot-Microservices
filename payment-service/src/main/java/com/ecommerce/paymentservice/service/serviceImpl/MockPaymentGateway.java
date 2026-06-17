package com.ecommerce.paymentservice.service.serviceImpl;

import com.ecommerce.paymentservice.dto.GatewayOrderResponse;
import com.ecommerce.paymentservice.service.PaymentGateway;
import com.razorpay.RazorpayException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Primary
public class MockPaymentGateway implements PaymentGateway {
    @Override
    public GatewayOrderResponse createOrder(String orderNumber, BigDecimal amount) throws RazorpayException {
        return new GatewayOrderResponse(
                "mock-order-" + UUID.randomUUID(),
                "INR",
                amount,
                "created",
                "MOCK",
                "receipt-test"
        );
    }
}

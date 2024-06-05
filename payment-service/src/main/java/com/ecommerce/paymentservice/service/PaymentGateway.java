package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.GatewayOrderResponse;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;

public interface PaymentGateway {

    GatewayOrderResponse createOrder(String orderId, BigDecimal amount) throws RazorpayException;
}

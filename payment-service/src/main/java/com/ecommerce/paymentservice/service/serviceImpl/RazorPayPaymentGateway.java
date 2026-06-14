package com.ecommerce.paymentservice.service.serviceImpl;

import com.ecommerce.paymentservice.dto.GatewayOrderResponse;
import com.ecommerce.paymentservice.service.PaymentGateway;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class RazorPayPaymentGateway implements PaymentGateway {

    private final RazorpayClient razorpayClient;

    @Override
    public GatewayOrderResponse createOrder(String orderNumber, BigDecimal amount) {
       try {
           JSONObject options = new JSONObject();
           options.put("amount", amount.multiply(BigDecimal.valueOf(100)));
           options.put("currency", "INR");
           options.put("receipt", orderNumber);
           Order order = razorpayClient.orders.create(options);
           log.info("Creating RazorPay Order for {} amount {}", orderNumber, amount);
           return new GatewayOrderResponse(order.get("id"), order.get("currency"), amount, order.get("status"), "RazorPay", order.get("receipt"));
       } catch (RazorpayException ex) {
           throw  new IllegalStateException("RazorPay Order Creation Failed");
       }
    }
}

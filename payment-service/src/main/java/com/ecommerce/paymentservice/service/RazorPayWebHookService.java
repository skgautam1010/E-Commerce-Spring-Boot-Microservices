package com.ecommerce.paymentservice.service;


import com.ecommerce.paymentservice.client.OrderClient;
import com.ecommerce.paymentservice.client.UserClient;
import com.ecommerce.paymentservice.dto.UserResponseDto;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.event.NotificationEvent;
import com.ecommerce.paymentservice.kafka.NotificationProducer;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ecommerce.paymentservice.entity.Payment;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorPayWebHookService {
    private final PaymentRepository paymentRespository;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final NotificationProducer notificationProducer;

    @Value("${razorpay.webhook-secret}")
    private String webHookSecret;

    @Transactional
    public void processWebHook(String signature, String payload) {
        verifySignature(signature, payload);
        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");
        log.info("Received RazorPay webhook event: " +  eventType);
        switch (eventType) {
            case "payment.captured":
                handlePaymentSuccess(event);
                break;
            case "payment.failed":
                handlePaymentFailure(event);
                break;
            default:
                log.warn("UnExpected Value: " + eventType);
        }
    }

    private void handlePaymentSuccess(JSONObject event) {
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorPayOrderId = paymentEntity.getString("order_id");
        String razorPayPaymentId = paymentEntity.getString("id");
        Payment payment = paymentRespository.findByGatewayOrderId(razorPayOrderId).orElseThrow(() -> new IllegalStateException("Payment Not Found for RazorPay Order Id"));
        if(payment.getGatewayPaymentId() != null && payment.getGatewayPaymentId().equals(razorPayPaymentId)) {
            log.info("Duplicate payment.captured webhook received for paymentId: {}", razorPayPaymentId);
            return;
        }
        markPaymentSuccess(payment, razorPayPaymentId);
    }
    private void handlePaymentFailure(JSONObject event) {
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorPayOrderId = paymentEntity.getString("order_id");
        String razorPayPaymentId = paymentEntity.getString("id");
        Payment payment = paymentRespository.findByGatewayOrderId(razorPayOrderId).orElseThrow(() -> new IllegalStateException("Payment Not Found for RazorPay Order Id"));
        if(payment.getGatewayPaymentId() != null && payment.getGatewayPaymentId().equals(razorPayPaymentId)) {
            log.info("Duplicate payment.failed webhook received for paymentId: {}", razorPayPaymentId);
            return;
        }
        markPaymentFailure(payment, razorPayPaymentId);
    }

    private JSONObject extractPaymentEntity(JSONObject event) {
        return event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
    }

    private void verifySignature(String signature, String payload) {
        try {
            Utils.verifyWebhookSignature(payload, signature, webHookSecret);
        } catch (Exception e) {
            log.error("Invalid RazorPay Webhook Signature");
            throw new SecurityException("Invalid RazorPay Webhook Signature");
        }
    }

    private void markPaymentSuccess(Payment payment, String razorPayPaymentId) {

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(razorPayPaymentId);
        payment.setPaidAt(LocalDateTime.now());

        paymentRespository.save(payment);

        UserResponseDto userResponseDto = userClient.getUserById(payment.getUserId());
        NotificationEvent event =
                NotificationEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .email(userResponseDto.getEmail())
                        .mobile(userResponseDto.getPhone())
                        .type("email")
                        .subject("Payment Successful")
                        .orderNumber(payment.getOrderNumber())
                        .amount(payment.getAmount())
                        .message(
                                "Your payment of ₹"
                                        + payment.getAmount()
                                        + " for order "
                                        + payment.getOrderNumber()
                                        + " has been successfully processed."
                        )
                        .eventTime(LocalDateTime.now())
                        .build();
        notificationProducer.sendNotification(event);
        orderClient.confirmOrder(payment.getOrderNumber());

    }


    private void markPaymentFailure(Payment payment, String razorPayPaymentId) {

        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setGatewayPaymentId(razorPayPaymentId);
        payment.setFailedAt(LocalDateTime.now());
        paymentRespository.save(payment);

        UserResponseDto userResponseDto = userClient.getUserById(payment.getUserId());
        NotificationEvent event =
                NotificationEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .email(userResponseDto.getEmail())
                        .mobile(userResponseDto.getPhone())
                        .type("email")
                        .subject("Payment Failed")
                        .orderNumber(payment.getOrderNumber())
                        .amount(payment.getAmount())
                        .message(
                                "Your payment of ₹"
                                        + payment.getAmount()
                                        + " for order "
                                        + payment.getOrderNumber()
                                        + " has failed. Please try again."
                        )
                        .eventTime(LocalDateTime.now())
                        .build();

        notificationProducer.sendNotification(event);
        orderClient.failOrder(payment.getOrderNumber());
    }

}

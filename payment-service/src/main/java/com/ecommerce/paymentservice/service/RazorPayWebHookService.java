package com.ecommerce.paymentservice.service;


import com.ecommerce.paymentservice.client.InventoryClient;
import com.ecommerce.paymentservice.client.OrderClient;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.repository.PaymentRespository;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ecommerce.paymentservice.entity.Payment;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorPayWebHookService {
    private final PaymentRespository paymentRespository;
    private final OrderClient orderClient;
    private final InventoryClient inventoryClient;
    @Value("${razorpay.webhook-secret}")
    private String webHookSecret;

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
        Payment payment = paymentRespository.findByTransactionId(razorPayOrderId).orElseThrow(() -> new IllegalStateException("Payment Not Found for RazorPay Order Id"));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(razorPayPaymentId);
        orderClient.confirmOrder(payment.getOrderId());
        inventoryClient.commitInventory(payment.getOrderId());
    }
    private void handlePaymentFailure(JSONObject event) {
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorPayOrderId = paymentEntity.getString("order_id");
        Payment payment = paymentRespository.findByTransactionId(razorPayOrderId).orElseThrow(() -> new IllegalStateException("Payment Not Found for RazorPay Order Id"));
        payment.setPaymentStatus(PaymentStatus.FAILED);
        orderClient.failedOrder(payment.getOrderId());
        inventoryClient.rollbackInventory(payment.getOrderId());
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

}

package com.ecommerce.paymentservice.repository;

import com.ecommerce.paymentservice.entity.Payment;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRespository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderNumber(String orderNumber);
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);
    Optional<Payment> findByPaymentReference(String paymentReference);
}

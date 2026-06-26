package com.ecommerce.paymentservice.repository;

import com.ecommerce.paymentservice.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderNumber(String orderNumber);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT p
       FROM Payment p
       WHERE p.gatewayOrderId = :gatewayOrderId
       """)
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);
    Optional<Payment> findByPaymentReference(String paymentReference);
}

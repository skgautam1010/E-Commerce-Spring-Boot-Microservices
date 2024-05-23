package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.ProcessedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedOrderRepository extends JpaRepository<ProcessedOrder, Long> {
    boolean findByOrderId(String orderId);
}

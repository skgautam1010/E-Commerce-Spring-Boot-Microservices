package com.ecommerce.paymentservice.client;

import com.ecommerce.paymentservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/internal/{id}")
    UserResponseDto getUserById(@PathVariable Long id);
}

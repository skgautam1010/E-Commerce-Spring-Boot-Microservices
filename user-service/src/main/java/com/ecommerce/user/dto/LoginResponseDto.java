package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private Long expires_in;
    private Instant expiresAt;
}

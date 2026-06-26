package com.ecommerce.user.mapper;


import com.ecommerce.user.dto.LoginResponseDto;import com.ecommerce.user.entity.User;
import com.ecommerce.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final JwtUtil jwtUtil;

    @Value("${app.jwt.expiration-ms}")
    private Long expirationMs;

    public LoginResponseDto toLoginResponse(User user, String token) {
        return LoginResponseDto.builder().token(token).tokenType("Bearer").userId(user.getId())
                .email(user.getEmail()).name(user.getName())
                .expiresAt(jwtUtil.getExpirationFromToken(token)).expires_in(expirationMs/1000).build();
    }
}

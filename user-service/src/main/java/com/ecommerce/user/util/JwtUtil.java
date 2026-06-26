package com.ecommerce.user.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;


@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms}") Long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    public String generateToken(Long userId, String emailId, String role) {
        Long now = System.currentTimeMillis();
        return Jwts.builder().subject(String.valueOf(userId))
                .claim("email", emailId)
                .claim("role",role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(secretKey).compact();
    }
    public Jws<Claims> validateToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = validateToken(token).getBody();
        return Long.valueOf(claims.getSubject());
    }

    public Instant getExpirationFromToken(String token) {
        Claims claims = validateToken(token).getBody();
        return claims.getExpiration().toInstant();
    }

}

package com.ecommerce.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private JWTUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/users/register",
            "/api/users/login",
            "/products/images"
    );


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        if(PUBLIC_URLS.stream().anyMatch(path :: contains)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }
        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token)) {
            return unauthorized(exchange);
        }
        String userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        if(!isAuthorized(role, path, method)) {
            return forbidden(exchange);
        }
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header("X-User-Id", userId).header("X-User-Role", role).build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private static final Map<String, Map<String, List<String>>> ROLE_BASED_PERMISSIONS = Map.of(
            "ADMIN", Map.of(
                    "GET", List.of("/api/products", "/api/category", "/api/orders", "/api/payments", "/api/users"),
                    "POST", List.of("/api/products", "/api/category", "/api/inventory"),
                    "PUT", List.of("/api/products", "/api/category", "/api/payments"),
                    "DELETE", List.of("/api/products", "/api/category")
            ),
            "ROLE_USER", Map.of(
                    "GET", List.of("/api/products", "/api/users", "/api/category", "/api/inventory", "/api/payments"),
                    "POST", List.of("/api/category", "/api/products", "/api/order", "/api/payment")
            )
    );

    public boolean isAuthorized(String role, String path, String method) {
        Map<String, List<String>> userRole = ROLE_BASED_PERMISSIONS.get(role);
        if(userRole == null) return false;
        List<String> allowedPaths = userRole.get(method);
        if(allowedPaths == null) return false;
        return allowedPaths.stream().anyMatch(path :: startsWith);
    }
}

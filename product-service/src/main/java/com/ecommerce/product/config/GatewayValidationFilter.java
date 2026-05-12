package com.ecommerce.product.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        if(request.getRequestURI().startsWith("/products/images")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check for the header your Gateway injects
        String userRole = request.getHeader("X-User-Role");
        // If the header is missing, it means the request bypassed the Gateway
        if (userRole == null || userRole.isBlank()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Direct access forbidden. Please use the API Gateway.\"}");
            return;
        }

        // If header exists, move to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}

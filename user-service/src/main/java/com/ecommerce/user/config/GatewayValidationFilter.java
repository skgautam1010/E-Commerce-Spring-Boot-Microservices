package com.ecommerce.user.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayValidationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String routeHeader = request.getHeader("X-Route-Header");
        String uri = request.getRequestURI();

        if((uri.startsWith("/api/users/register") || uri.startsWith("/api/users/login")) && StringUtils.isNotBlank(routeHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userRole = request.getHeader("X-User-Role");
        // If the header is missing, it means the request bypassed the Gateway
        String internalService = request.getHeader("X-Internal-Service");
        boolean gatewayRequest =
                userRole != null && !userRole.isBlank();

        boolean internalRequest =
                internalService != null && !internalService.isBlank();

        if (!gatewayRequest && !internalRequest) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Direct access forbidden. Please use the API Gateway.\"}");
            return;
        }
        filterChain.doFilter(request, response);

    }
}

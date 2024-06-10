package com.ecommerce.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomRouteFilter extends AbstractGatewayFilterFactory<Object> {

    public CustomRouteFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
           System.out.println("Route Filter : Before Routing");
           exchange.getRequest().mutate().header("X-Route-Header","Added-By-Route-Filter").build();
           return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                System.out.println("Route Filter: After Routing");
           }));
        });
    }
}

package com.ecommerce.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomRouteFilter extends AbstractGatewayFilterFactory<Object> {

    private static final Logger log = (Logger) LoggerFactory.getLogger(CustomRouteFilter.class);

    public CustomRouteFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
           log.info("Route Filter : Before Routing");
            var mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Route-Header", "Added-By-Route-Filter")
                    .build();

            var mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

           return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
                log.info("Route Filter: After Routing");
           }));
        });
    }
}

package com.ruberoo.api_gateway.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> implements GatewayFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String BEARER = "Bearer ";

    public JwtValidationFilter() {
        super(Config.class);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. Check for Authorization header
        if (!request.getHeaders().containsKey("Authorization")) {
            return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
        }

        List<String> headers = request.getHeaders().get("Authorization");
        String token = headers.get(0).replace(BEARER, "");

        // 2. Validate token
        if (!jwtTokenProvider.validateToken(token)) {
            return this.onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
        }

        // 3. Add user info to request header for downstream services
        // This is crucial: we pass the validated user identity (username/ID)
        // to the microservice so it knows who is making the request.
        String username = jwtTokenProvider.getUsernameFromToken(token);

        exchange.getRequest().mutate()
                .header("X-Auth-User", username)
                .build();

        // 4. Continue the filter chain
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    // Required for AbstractGatewayFilterFactory
    @Override
    public GatewayFilter apply(Config config) {
        return this;
    }

    public static class Config {
        // Empty class needed for AbstractGatewayFilterFactory
    }
}


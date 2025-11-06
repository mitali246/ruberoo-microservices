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
        String path = request.getURI().getPath();

        // Skip JWT validation for public endpoints
        if (isPublicPath(path, request.getMethod().toString())) {
            return chain.filter(exchange);
        }

        // 1. Check for Authorization header
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty()) {
            return this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeaders.get(0).replace(BEARER, "");

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

    // Helper method to check if path is public
    private boolean isPublicPath(String path, String method) {
        // Public paths that don't require authentication
        if (path.startsWith("/api/users/auth/")) {
            return true; // Login endpoint
        }
        if (path.equals("/api/users") && "POST".equalsIgnoreCase(method)) {
            return true; // Registration endpoint (POST only)
        }
        if (path.startsWith("/actuator/")) {
            return true; // Health checks
        }
        if (path.startsWith("/eureka/")) {
            return true; // Eureka endpoints
        }
        return false;
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


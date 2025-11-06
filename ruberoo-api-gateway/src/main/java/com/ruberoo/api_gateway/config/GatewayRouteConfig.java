package com.ruberoo.api_gateway.config;

import com.ruberoo.api_gateway.jwt.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Autowired
    private JwtValidationFilter jwtValidationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config())))
                        .uri("lb://user-service"))
                
                // Ride Management Service Routes
                .route("ride-management-service", r -> r
                        .path("/api/rides/**")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config())))
                        .uri("lb://ride-management-service"))
                
                // Tracking Service Routes
                .route("tracking-service", r -> r
                        .path("/api/tracking/**")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config())))
                        .uri("lb://tracking-service"))
                
                .build();
    }
}


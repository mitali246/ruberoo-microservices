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
                // User Service Routes - routes /api/users/** to user-service
                // JWT filter will skip validation for public paths (/api/users/auth/** and POST /api/users)
                .route("user-service-route", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config())))
                        .uri("lb://USER-SERVICE"))
                
                // Ride Management Service Routes
                .route("ride-management-service-route", r -> r
                        .path("/api/rides/**")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config())))
                        .uri("lb://RIDE-MANAGEMENT-SERVICE"))
                
                // Tracking Service Routes
                .route("tracking-service-route", r -> r
                        .path("/api/tracking/**")
                        .filters(f -> f.filter(jwtValidationFilter.apply(new JwtValidationFilter.Config())))
                        .uri("lb://TRACKING-SERVICE"))
                
                .build();
    }
}


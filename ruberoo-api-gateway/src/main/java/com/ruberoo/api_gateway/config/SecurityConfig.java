package com.ruberoo.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        // We are disabling CSRF because we are using JWT tokens (stateless authentication)
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // Allow access to Eureka, Config Server, and public endpoints (order matters!)
                        .pathMatchers("/actuator/**").permitAll() // For health checks - must be first
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/users/auth/**").permitAll() // Login endpoint
                        .pathMatchers("/api/users").permitAll() // Registration endpoint (POST /api/users)

                        // All other requests must be authenticated
                        .anyExchange().authenticated()
                )
                // Disable default form login and HTTP basic - we use JWT
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}

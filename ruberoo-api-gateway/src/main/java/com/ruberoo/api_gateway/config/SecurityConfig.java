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
                        // Allow access to ALL endpoints for testing/demo
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/**").permitAll() // Allow ALL API endpoints
                        .pathMatchers("/**").permitAll() // Allow everything for demo
                        .anyExchange().permitAll()
                )
                // Disable default form login and HTTP basic - we use JWT
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}

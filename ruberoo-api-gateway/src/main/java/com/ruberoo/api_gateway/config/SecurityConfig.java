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
                        // Allow access to Eureka, Config Server, and the public login endpoint
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll() // For health checks
                        .pathMatchers("/api/users/auth/**").permitAll() // User authentication (login/register)

                        // All other requests must be authenticated
                        .anyExchange().authenticated()
                )
                .build();
    }
}

package com.ruberoo.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;
// ServerWebExchange is included via the KeyResolver's signature.

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // 🚨 NEW CODE FOR RATE LIMITING (Lambda Expression) 🚨
    // Defines a bean named 'ipKeyResolver' that is referenced in api-gateway.yml
    @Bean
    public KeyResolver ipKeyResolver() {
        // This is the clean, functional way to implement the KeyResolver interface.
        // It resolves the key using the client's host IP address.
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}

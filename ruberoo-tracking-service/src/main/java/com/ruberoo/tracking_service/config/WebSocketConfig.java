package com.ruberoo.tracking_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket Configuration for Real-Time GPS Tracking
 * Enables STOMP over WebSocket for bidirectional communication
 * 
 * Endpoints:
 * - /ws/tracking - WebSocket handshake endpoint
 * - /topic/tracking/{rideId} - Subscribe to ride location updates
 * - /app/tracking/update - Send location updates from drivers
 * 
 * @author Ruberoo Team
 * @version 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configure message broker for pub/sub messaging
     * - Simple broker handles /topic destinations for broadcasting
     * - Application destination prefix /app for client-to-server messages
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple in-memory message broker for pub/sub
        // Production should use RabbitMQ or Redis for scalability
        config.enableSimpleBroker("/topic");
        
        // Set prefix for messages bound for @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Register STOMP endpoints for WebSocket handshake
     * - Endpoint: /ws/tracking
     * - Allows connections from any origin (configure CORS as needed)
     * - Supports SockJS fallback for browsers without WebSocket support
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/tracking")
                .setAllowedOriginPatterns("*") // TODO: Configure specific origins for production
                .withSockJS(); // Fallback for browsers without WebSocket support
    }
}

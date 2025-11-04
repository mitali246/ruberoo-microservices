package com.ruberoo.tracking_service.controller;

import com.ruberoo.tracking_service.dto.LocationUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * WebSocket Controller for Real-Time GPS Tracking
 * Handles bidirectional location updates between drivers and riders
 * 
 * WebSocket Endpoints:
 * - /app/tracking/update/{rideId} - Drivers send location updates
 * - /topic/tracking/{rideId} - Riders subscribe to location updates
 * 
 * REST Endpoints:
 * - GET /api/tracking/health - Health check
 * - GET /api/tracking/rides/{rideId}/location - Get last known location
 * 
 * @author Ruberoo Team
 * @version 1.0
 */
@Controller
@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "*") // TODO: Configure specific origins for production
public class TrackingWebSocketController {
    
    private static final Logger logger = LoggerFactory.getLogger(TrackingWebSocketController.class);
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public TrackingWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * WebSocket Message Mapping: Driver sends location update
     * Message sent to: /app/tracking/update/{rideId}
     * Broadcast to: /topic/tracking/{rideId}
     * 
     * @param rideId The ride identifier
     * @param locationUpdate GPS location data from driver
     * @return Location update to be broadcast to all subscribers
     */
    @MessageMapping("/tracking/update/{rideId}")
    @SendTo("/topic/tracking/{rideId}")
    public LocationUpdateDto handleLocationUpdate(
            @DestinationVariable Long rideId,
            @Payload LocationUpdateDto locationUpdate) {
        
        logger.info("Received location update for ride {}: lat={}, lon={}, speed={}", 
                    rideId, locationUpdate.getLatitude(), locationUpdate.getLongitude(), 
                    locationUpdate.getSpeed());
        
        // Set timestamp and rideId
        locationUpdate.setRideId(rideId);
        locationUpdate.setTimestamp(LocalDateTime.now());
        
        // Validate location data
        if (!isValidLocation(locationUpdate)) {
            logger.warn("Invalid location data received for ride {}", rideId);
            return null;
        }
        
        // TODO: Persist location to database for historical tracking
        // TODO: Calculate ETA and distance remaining
        // TODO: Detect route deviation and send alerts
        
        return locationUpdate;
    }
    
    /**
     * Programmatic method to send location updates
     * Used by backend services to push updates
     * 
     * @param rideId The ride identifier
     * @param locationUpdate GPS location data
     */
    public void sendLocationUpdate(Long rideId, LocationUpdateDto locationUpdate) {
        logger.info("Broadcasting location update for ride {}", rideId);
        messagingTemplate.convertAndSend("/topic/tracking/" + rideId, locationUpdate);
    }
    
    /**
     * REST Endpoint: Get last known location for a ride
     * 
     * @param rideId The ride identifier
     * @return Last known location or 404 if not found
     */
    @GetMapping("/rides/{rideId}/location")
    public ResponseEntity<LocationUpdateDto> getLastKnownLocation(@PathVariable Long rideId) {
        logger.info("REST request for last known location of ride {}", rideId);
        
        // TODO: Retrieve from cache (Redis) or database
        // For now, return not found
        return ResponseEntity.notFound().build();
    }
    
    /**
     * REST Endpoint: Health check
     * 
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Tracking WebSocket service is running");
    }
    
    /**
     * Validate location data
     * 
     * @param location Location data to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidLocation(LocationUpdateDto location) {
        if (location == null) {
            return false;
        }
        
        // Validate latitude (-90 to 90)
        if (location.getLatitude() == null || 
            location.getLatitude() < -90 || location.getLatitude() > 90) {
            return false;
        }
        
        // Validate longitude (-180 to 180)
        if (location.getLongitude() == null || 
            location.getLongitude() < -180 || location.getLongitude() > 180) {
            return false;
        }
        
        // Validate speed (0 to 200 km/h reasonable max)
        if (location.getSpeed() != null && 
            (location.getSpeed() < 0 || location.getSpeed() > 200)) {
            return false;
        }
        
        // Validate heading (0 to 360 degrees)
        if (location.getHeading() != null && 
            (location.getHeading() < 0 || location.getHeading() > 360)) {
            return false;
        }
        
        return true;
    }
}

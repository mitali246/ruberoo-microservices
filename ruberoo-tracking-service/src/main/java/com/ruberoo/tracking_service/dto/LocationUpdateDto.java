package com.ruberoo.tracking_service.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for GPS Location Updates
 * Used for real-time location streaming via WebSocket
 * 
 * @author Ruberoo Team
 * @version 1.0
 */
public class LocationUpdateDto {
    
    private Long rideId;
    private Long driverId;
    private Double latitude;
    private Double longitude;
    private Double speed; // km/h
    private Double heading; // degrees (0-360)
    private LocalDateTime timestamp;
    private String status; // MOVING, STOPPED, IDLE
    
    // Default constructor
    public LocationUpdateDto() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Full constructor
    public LocationUpdateDto(Long rideId, Long driverId, Double latitude, Double longitude, 
                            Double speed, Double heading, String status) {
        this.rideId = rideId;
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getRideId() {
        return rideId;
    }
    
    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
    
    public Long getDriverId() {
        return driverId;
    }
    
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public Double getSpeed() {
        return speed;
    }
    
    public void setSpeed(Double speed) {
        this.speed = speed;
    }
    
    public Double getHeading() {
        return heading;
    }
    
    public void setHeading(Double heading) {
        this.heading = heading;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "LocationUpdateDto{" +
                "rideId=" + rideId +
                ", driverId=" + driverId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", heading=" + heading +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }
}

package com.ruberoo.ride_management_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;
    private String scheduledTime;

    // Manually-added Getters
    public Long getId() { return id; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getScheduledTime() { return scheduledTime; }

    // Manually-added Setters
    public void setId(Long id) { this.id = id; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }
}

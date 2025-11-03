package com.ruberoo.ride_management_service.dto;

import com.ruberoo.ride_management_service.entity.Ride;
import com.ruberoo.ride_management_service.entity.User; // Replica of the User entity

public class RideResponseDTO {
    // Fields from the Ride entity
    private Long id;
    private String source;
    private String destination;
    private String status;
    private Long userId;

    // Field for the User details fetched from User Service
    private User user;

    // Constructors, Getters, and Setters
    // ... (You will need to implement these)
}

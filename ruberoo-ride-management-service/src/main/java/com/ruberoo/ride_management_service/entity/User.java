package com.ruberoo.ride_management_service.entity;

// IMPORTANT: This is NOT a JPA entity; it's a simple POJO for Feign deserialization.
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    // Add any other fields you need from the User Service here!

    // Default (no-args) constructor is essential for JSON deserialization by Feign/Jackson
    public User() {
    }

    // You should also add a parameterized constructor and all your Getters and Setters!

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // ... (rest of the getters and setters)
}
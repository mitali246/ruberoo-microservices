package com.ruberoo.user_service.dto;

public class LoginRequest {
    private String email;
    private String password;

    // Getters and Setters (essential for Spring to map the JSON payload)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


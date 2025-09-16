package com.ruberoo.tracking_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // To link the contact to a user
    private String contactName;
    private String contactNumber;

    // Manually-added Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getContactName() { return contactName; }
    public String getContactNumber() { return contactNumber; }

    // Manually-added Setters
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}

package com.ruberoo.tracking_service.repository;

import com.ruberoo.tracking_service.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
}

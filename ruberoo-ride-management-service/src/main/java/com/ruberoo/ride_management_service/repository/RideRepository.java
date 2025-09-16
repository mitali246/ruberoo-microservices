package com.ruberoo.ride_management_service.repository;

import com.ruberoo.ride_management_service.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
}

package com.ruberoo.ride_management_service.service;

import com.ruberoo.ride_management_service.entity.Ride;
import com.ruberoo.ride_management_service.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride updateRide(Long id, Ride rideDetails) {
        Ride ride = rideRepository.findById(id).orElse(null);
        if (ride != null) {
            ride.setOrigin(rideDetails.getOrigin());
            ride.setDestination(rideDetails.getDestination());
            ride.setScheduledTime(rideDetails.getScheduledTime());
            return rideRepository.save(ride);
        }
        return null;
    }

    public void deleteRide(Long id) {
        rideRepository.deleteById(id);
    }
}

package com.ruberoo.ride_management_service.controller;

import com.ruberoo.ride_management_service.entity.Ride;
import com.ruberoo.ride_management_service.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping
    public Ride createRide(@RequestBody Ride ride) {
        return rideService.createRide(ride);
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    @GetMapping("/{id}")
    public Ride getRideById(@PathVariable Long id) {
        return rideService.getRideById(id);
    }

    @PutMapping("/{id}")
    public Ride updateRide(@PathVariable Long id, @RequestBody Ride rideDetails) {
        return rideService.updateRide(id, rideDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
    }
}









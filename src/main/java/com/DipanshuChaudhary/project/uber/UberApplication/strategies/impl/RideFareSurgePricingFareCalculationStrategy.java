package com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import com.DipanshuChaudhary.project.uber.UberApplication.services.DistanceService;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    // IN CASE THERE IS ANY OTHER FACTOR LIKE RAINING etc. Then increase the price with this
    private static final double SURGE_FACTOR = 2;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(),
                rideRequest.getDropOffLocation());

        return distance*RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
    }
}

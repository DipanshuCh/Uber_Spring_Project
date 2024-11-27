package com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import com.DipanshuChaudhary.project.uber.UberApplication.services.DistanceService;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RideFareDefaultFareCalculationStrategyImpl implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {

        double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(),
                rideRequest.getDropOffLocation());


        return distance * RIDE_FARE_MULTIPLIER;
    }

}

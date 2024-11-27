package com.DipanshuChaudhary.project.uber.UberApplication.strategies;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;

public interface RideFareCalculationStrategy {


    double RIDE_FARE_MULTIPLIER = 10;


    double calculateFare(RideRequest rideRequest);
}

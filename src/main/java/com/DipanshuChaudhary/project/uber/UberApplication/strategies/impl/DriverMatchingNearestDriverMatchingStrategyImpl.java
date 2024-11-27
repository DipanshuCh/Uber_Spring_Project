package com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.DriverRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverMatchingStrategyImpl implements DriverMatchingStrategy {


    private final DriverRepository driverRepository;


    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        // we pass only pickupLocation used to find the driver
        return driverRepository.findTenNearestDrivers(rideRequest.getPickUpLocation());
    }
}

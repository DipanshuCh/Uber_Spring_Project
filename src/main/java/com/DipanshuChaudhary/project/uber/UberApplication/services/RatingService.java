package com.DipanshuChaudhary.project.uber.UberApplication.services;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.DriverDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RiderDto;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Ride;

public interface RatingService {

    DriverDto rateDriver(Ride ride, Integer rating);

    RiderDto rateRider(Ride ride, Integer rating);

    void createNewRating(Ride ride);


}

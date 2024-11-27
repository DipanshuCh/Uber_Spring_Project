package com.DipanshuChaudhary.project.uber.UberApplication.services;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.DriverDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RideDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RideRequestDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RiderDto;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Rider;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    // create method to create a new Rider
    Rider createNewRider(User user);

    Rider getCurrentRider();

}

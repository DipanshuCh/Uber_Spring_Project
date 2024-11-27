package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Ride;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Rider;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideRequestStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.ResourceNotFoundException;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.RideRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RideRequestService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: "+rideId));
    }

    @Override
    @Transactional
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        Ride ride = modelMapper.map(rideRequest,Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOTP(generateRandomOTP());
        ride.setId(null);

        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }


    private String generateRandomOTP(){
        Random random = new Random();
        int otpInt = random.nextInt(10000); // 0 to 9999
        return String.format("%04d", otpInt);
    }
}

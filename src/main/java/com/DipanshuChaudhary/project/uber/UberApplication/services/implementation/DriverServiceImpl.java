package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.DriverDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RideDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RiderDto;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Ride;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.User;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideRequestStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.ResourceNotFoundException;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.DriverRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;


    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new RuntimeException("RideRequest cannot be accepted, status is " +
                    rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }

//        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        Driver savedDriver = updateDriverAvailability(currentDriver,false);

        Ride ride = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(ride, RideDto.class);

    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);

        // after cancelling the ride driver availability can be true
        updateDriverAvailability(driver,true);

        return modelMapper.map(ride,RideDto.class);
    }

    @Override
    public RideDto startRide(Long rideId, String Otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride status is not CONFIRMED hence cannot be started, status "+ride.getRideStatus());
        }

        if (!Otp.equals(ride.getOTP())){
            throw new RuntimeException("Otp is not valid, otp "+Otp);
        }

        ride.setRideStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);

        return modelMapper.map(savedRide, RideDto.class);

    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot end a ride as he has not accepted it earlier");
        }

        if (!ride.getRideStatus().equals(RideStatus.ONGOING )){
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status "+ride.getRideStatus());
        }

        // this is the current end time of ride
        ride.setRideEndedAt(LocalDateTime.now());

        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ENDED);
        updateDriverAvailability(driver,true);

        paymentService.processPayment(ride);

        return modelMapper.map(savedRide,RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not the owner of this Ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is nto ENDED hence cannot start rating, status: "+ride.getRideStatus());
        }

        return ratingService.rateRider(ride,rating);


    }

    @Override
    public DriverDto getMyProfile() {
        Driver driver = getCurrentDriver();
        return modelMapper.map(driver,DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver driver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(driver,pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not associated with user with id: " + user.getId()));
        return driver;
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        Driver savedDriver = driverRepository.save(driver);
        return savedDriver;
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}

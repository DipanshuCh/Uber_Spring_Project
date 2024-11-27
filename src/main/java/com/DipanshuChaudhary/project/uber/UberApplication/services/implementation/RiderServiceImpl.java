package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.dto.DriverDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RideDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RideRequestDto;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.RiderDto;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.*;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideRequestStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.ResourceNotFoundException;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.RideRequestRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.RiderRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.DriverService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RatingService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RideService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RiderService;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    // modal mapper is used for mapping one object to another type
    private final ModelMapper modelMapper;

    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;


//    @Override
////    @Transactional
//    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
//
//        Rider rider = getCurrentRider();
//        RideRequest rideRequestEntity = modelMapper.map(rideRequestDto, RideRequest.class);
//        rideRequestEntity.setRideRequestStatus(RideRequestStatus.PENDING);
//
//        // set the rider
//        rideRequestEntity.setRider(rider);
//
//
//        // calculate the fare
//        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequestEntity);
//        rideRequestEntity.setFare(fare);
//
//
//        RideRequest savedRideRequest = rideRequestRepository.save(rideRequestEntity);
//
//        rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequestEntity);
//
//        return modelMapper.map(savedRideRequest, RideRequestDto.class);
//
//    }

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = rideStrategyManager
                .driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

//        TODO : Send notification to all the drivers about this ride request

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Ride does not own this ride with id: "+rideId);
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {

        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if (!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider is not the owner of this Ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is nto ENDED hence cannot start rating, status: "+ride.getRideStatus());
        }

        return ratingService.rateDriver(ride,rating);

    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider,RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest){
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider ,pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {

        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();

        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {


        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Rider not associated with user with id: " + user.getId()
        ));
    }
}

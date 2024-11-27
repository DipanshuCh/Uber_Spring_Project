package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.ResourceNotFoundException;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.RideRequestRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;


    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id: "+rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {
        rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id: "+rideRequest.getId()));
        rideRequestRepository.save(rideRequest);
    }
}

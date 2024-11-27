package com.DipanshuChaudhary.project.uber.UberApplication.controllers;

import com.DipanshuChaudhary.project.uber.UberApplication.advices.ApiResponse;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.*;
import com.DipanshuChaudhary.project.uber.UberApplication.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
@Secured("ROLE_DRIVER")
public class DriverController  {

    private final DriverService driverService;

    // this method used ot accept a ride

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId){
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }



//    @PostMapping("/startRide/{rideRequestId}")
//    public ResponseEntity<RideDto> startRide(@PathVariable Long rideRequestId,
//                                              @RequestBody RideStartDto rideStartDto){
//
//        return ResponseEntity.ok(driverService.startRide(rideRequestId,rideStartDto.getOtp()));
//
//    }




    // this method used to start a ride

    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<ApiResponse<RideDto>> startRide(@PathVariable Long rideRequestId,
                                                          @RequestBody RideStartDto rideStartDto) {

        RideDto rideDto = driverService.startRide(rideRequestId, rideStartDto.getOtp());

        ApiResponse<RideDto> response = new ApiResponse<>(rideDto);  // No error, so error is null
        return ResponseEntity.ok(response);  // Response with data, timestamp, and no error
    }



    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<ApiResponse<RideDto>> endRide(@PathVariable Long rideId) {
//        return ResponseEntity.ok(driverService.endRide(rideId));  // Response with data, timestamp, and no error

        RideDto rideDto = driverService.endRide(rideId);
        ApiResponse<RideDto> response = new ApiResponse<>(rideDto);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }


    @PostMapping("/rateRider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto){
        return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));
    }


    @GetMapping
    public ResponseEntity<DriverDto> getMyProfile(){
        return ResponseEntity.ok(driverService.getMyProfile());
    }


    @GetMapping("/getMyRides")
    public  ResponseEntity<ApiResponse<Page<RideDto>>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize){

//        PageRequest pageRequest = PageRequest.of(pageOffSet,pageSize);
//        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));


        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize
                                    , Sort.by(Sort.Direction.DESC, "createdTime", "id"));

        // Get paginated data from the service
        Page<RideDto> ridesPage = driverService.getAllMyRides(pageRequest);

        // Construct ApiResponse with the retrieved data
        ApiResponse<Page<RideDto>> response = new ApiResponse<>(ridesPage);

        return ResponseEntity.ok(response);

    }


 /*

    @PostMapping("/rateRider/{rideId}/{rating}")
    public ResponseEntity<ApiResponse<RiderDto>> rateRider(@PathVariable Long rideId, @PathVariable Integer rating){

        RiderDto riderDto = driverService.rateRider(rideId, rating);

        ApiResponse<RiderDto> response = new ApiResponse<>(riderDto);

        return ResponseEntity.ok(response);
    }

    */

}




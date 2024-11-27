package com.DipanshuChaudhary.project.uber.UberApplication.controllers;


import com.DipanshuChaudhary.project.uber.UberApplication.advices.ApiResponse;
import com.DipanshuChaudhary.project.uber.UberApplication.dto.*;
import com.DipanshuChaudhary.project.uber.UberApplication.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {

    private final RiderService riderService;


//    @PostMapping("/requestRide")
//    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto){
//
//        // here we are using ResponseEntity.ok() Status i.e. 200 OK
//        return ResponseEntity.ok(riderService.requestRide(rideRequestDto));
//    }


    @PostMapping("/requestRide")
    public ResponseEntity<ApiResponse<RideRequestDto>> requestRide(@RequestBody RideRequestDto rideRequestDto) {

        // Call the service to handle ride request logic
        RideRequestDto requestedRide = riderService.requestRide(rideRequestDto);

        // Wrap the result in an ApiResponse object
        ApiResponse<RideRequestDto> response = new ApiResponse<>(requestedRide);

        // Return the ApiResponse object
        return ResponseEntity.ok(response);
    }


    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }


    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto){
        return ResponseEntity.ok(riderService.rateDriver(ratingDto.getRideId(), ratingDto.getRating()));
    }


    @GetMapping
    public ResponseEntity<RiderDto> getMyProfile(){
        return ResponseEntity.ok(riderService.getMyProfile());
    }


    @GetMapping("/getMyRides")
    public  ResponseEntity<ApiResponse<Page<RideDto>>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize){

//        PageRequest pageRequest = PageRequest.of(pageOffSet,pageSize);
//        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));


        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize);

        // Get paginated data from the service
        Page<RideDto> ridesPage = riderService.getAllMyRides(pageRequest);

        // Construct ApiResponse with the retrieved data
        ApiResponse<Page<RideDto>> response = new ApiResponse<>(ridesPage);

        return ResponseEntity.ok(response);

    }


    /*
    @PostMapping("/rateDriver/{rideId}/{rating}")
    public ResponseEntity<ApiResponse<DriverDto>> rateDriver(@PathVariable Long rideId, @PathVariable Integer rating){

        DriverDto riderDto = riderService.rateDriver(rideId, rating);

        ApiResponse<DriverDto> response = new ApiResponse<>(riderDto);

        return ResponseEntity.ok(response);
    }
*/


}














package com.DipanshuChaudhary.project.uber.UberApplication.dto;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    private Long id;
    private PointDto pickUpLocation;
    private PointDto dropOffLocation;

    private LocalDateTime createdTime;
    private RiderDto rider;
    private DriverDto driver;
    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;

    private String otp;

    private Double fare;
    private LocalDateTime rideStartedAt;
    private LocalDateTime rideEndedAt;
}

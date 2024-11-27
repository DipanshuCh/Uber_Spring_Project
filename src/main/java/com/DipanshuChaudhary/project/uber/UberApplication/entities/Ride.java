package com.DipanshuChaudhary.project.uber.UberApplication.entities;


import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_ride_rider", columnList = "rider_id"),
        @Index(name = "idx_ride_driver", columnList = "driver_id")
})
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickUpLocation;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    private Double fare;

    private String OTP;


    // it is actually the time when the driver put the OTP and start the ride

//    @CreationTimestamp
    private LocalDateTime rideStartedAt;

    //it is actually the time when the driver reached the destination and stop the ride

//    @CreationTimestamp
    private LocalDateTime rideEndedAt;


}

package com.DipanshuChaudhary.project.uber.UberApplication.entities;


import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_rideRequest_rider", columnList = "rider_id")
})
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point pickUpLocation;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point dropOffLocation;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    private LocalDateTime requestedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;

    private Double fare;

}

package com.DipanshuChaudhary.project.uber.UberApplication.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(indexes = {
        @Index(name = "idx_rating_rider", columnList = "rider_id"),
        @Index(name = "idx_rating_driver", columnList = "driver_id")
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ride ride;

    @ManyToOne
    private Rider rider;

    @ManyToOne
    private Driver driver;

    private Integer driverRating; // rating for the driver

    private Integer riderRating;  // rating for the rider

}

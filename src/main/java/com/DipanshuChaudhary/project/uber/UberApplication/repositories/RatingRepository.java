package com.DipanshuChaudhary.project.uber.UberApplication.repositories;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Rating;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Ride;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);
    Optional<Rating> findByRide(Ride ride);

}

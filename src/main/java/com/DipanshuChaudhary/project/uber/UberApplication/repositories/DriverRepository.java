package com.DipanshuChaudhary.project.uber.UberApplication.repositories;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


// ST_Distance(point1, point2)
// ST_DWithin(point1, 10000)


@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    @Query(value = "Select d.*, ST_Distance(d.current_location, :pickUpLocation) AS distance " +
            "FROM driver d " +
            "WHERE d.available = true AND  ST_DWithin(d.current_location, :pickUpLocation, 10000) " +
            "ORDER BY distance " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(Point pickUpLocation);



    @Query(value = "Select d.* " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickUpLocation, 15000) " +
            "ORDER BY d.rating DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearByTopRatedDrivers(Point pickUpLocation);

    Optional<Driver> findByUser(User user);

}

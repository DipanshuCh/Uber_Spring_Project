package com.DipanshuChaudhary.project.uber.UberApplication.repositories;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

}

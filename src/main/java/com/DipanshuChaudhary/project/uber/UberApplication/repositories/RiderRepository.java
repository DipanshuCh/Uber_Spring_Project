package com.DipanshuChaudhary.project.uber.UberApplication.repositories;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Rider;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider,Long> {

    Optional<Rider> findByUser(User user);

}

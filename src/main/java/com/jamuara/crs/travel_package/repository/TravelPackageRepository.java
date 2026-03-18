package com.jamuara.crs.travel_package.repository;

import com.jamuara.crs.model.TravelPackage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
    @EntityGraph(attributePaths = {"hotelReservations", "reservations"})
    Optional<TravelPackage> findByUserProfileId(Long id);
}

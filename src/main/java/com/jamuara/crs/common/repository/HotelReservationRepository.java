package com.jamuara.crs.common.repository;

import com.jamuara.crs.model.HotelReservation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Profile("!nodb")
public interface HotelReservationRepository extends JpaRepository<HotelReservation, Long> {

    @EntityGraph(attributePaths = {"guests"})
    Optional<List<HotelReservation>> findByUserProfileId(Long id);

    @EntityGraph(attributePaths = {"guests"})
    List<HotelReservation> findByKcUserId(String id);
}

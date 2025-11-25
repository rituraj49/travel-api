package com.jamuara.crs.common.repository;

import com.jamuara.crs.model.Reservation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Profile("!nodb")
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

//    List<Reservation> findReservationByTravelerNameContainingIgnoreCase(String name);
    List<Reservation> findReservationByBookingStatus(Reservation.BookingStatus status);

    Optional<Reservation> findReservationByBookingId(String id);

    Optional<Reservation> findReservationByPnr(String pnr);

    Page<Reservation> findReservationByBookingStatus(Reservation.BookingStatus status, Pageable pageable);

}


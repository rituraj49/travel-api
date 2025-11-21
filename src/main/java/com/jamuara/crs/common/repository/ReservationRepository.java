package com.jamuara.crs.common.repository;

import com.jamuara.crs.model.Reservation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Profile("!nodb")
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

//    List<Reservation> findReservationByTravelerNameContainingIgnoreCase(String name);
    List<Reservation> findReservationByBookingStatus(Reservation.BookingStatus status);

    Optional<Reservation> findReservationByBookingId(String id);

    @Query("""
            SELECT DISTINCT r 
            FROM Reservation r
            LEFT JOIN FETCH r.travelers
            LEFT JOIN FETCH r.flightLegs
            WHERE r.bookingId=:id
            """)
    Optional<Reservation> findReservationWithAllRelations(@Param("id") String id);

    Optional<Reservation> findReservationByPnr(String pnr);
}


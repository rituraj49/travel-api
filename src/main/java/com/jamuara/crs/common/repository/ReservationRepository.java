package com.jamuara.crs.common.repository;

import com.jamuara.crs.model.Reservation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Profile("!nodb")
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

//    List<Reservation> findReservationByTravelerNameContainingIgnoreCase(String name);
    List<Reservation> findReservationByBookingStatus(Reservation.BookingStatus status);

//    @Query("""
//            SELECT r FROM reservation
//            LEFT JOIN FETCH r.payment
//            WHERE r.payment_id=:id
//            """)
    Optional<List<Reservation>> findByPaymentId(Long id);

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

    Page<Reservation> findReservationByBookingStatus(Reservation.BookingStatus status, Pageable pageable);



    Page<Reservation> findByCreatedAtBetweenAndBookingStatus(
            LocalDateTime from,
            LocalDateTime to,
            Reservation.BookingStatus status,
            Pageable pageable
    );
}


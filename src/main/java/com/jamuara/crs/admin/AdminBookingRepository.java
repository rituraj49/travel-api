package com.jamuara.crs.admin;

import com.jamuara.crs.model.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jamuara.crs.admin.AdminBooking;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminBookingRepository extends JpaRepository<Reservation, Long> {

    //  TODAY
    @Query(value = """
        SELECT * FROM reservation
        WHERE TO_DATE(last_ticket_date, 'YYYY-MM-DD') = CURRENT_DATE
        AND booking_status = :status
        """, nativeQuery = true)
    List<Reservation> findToday(@Param("status") String status);


    //  THIS WEEK
    @Query(value = """
        SELECT * FROM reservation
        WHERE TO_DATE(last_ticket_date, 'YYYY-MM-DD') BETWEEN 
              DATE_TRUNC('week', CURRENT_DATE)::DATE
          AND (DATE_TRUNC('week', CURRENT_DATE) + INTERVAL '6 day')::DATE
        AND booking_status = :status
        """, nativeQuery = true)
    List<Reservation> findThisWeek(@Param("status") String status);


    //  THIS MONTH
    @Query(value = """
        SELECT * FROM reservation
        WHERE TO_DATE(last_ticket_date, 'YYYY-MM-DD') BETWEEN 
              DATE_TRUNC('month', CURRENT_DATE)::DATE
          AND (DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day')::DATE
        AND booking_status = :status
        """, nativeQuery = true)
    List<Reservation> findThisMonth(@Param("status") String status);
}

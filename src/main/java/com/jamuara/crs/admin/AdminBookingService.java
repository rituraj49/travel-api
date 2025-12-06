package com.jamuara.crs.admin;

import com.jamuara.crs.model.Reservation;
import org.springframework.stereotype.Service;
import com.jamuara.crs.admin.AdminBooking;

import java.util.List;

public interface AdminBookingService {
    List<Reservation> today(Reservation.BookingStatus status);

    List<Reservation> weekly(Reservation.BookingStatus status);

    List<Reservation> monthly(Reservation.BookingStatus status);
}

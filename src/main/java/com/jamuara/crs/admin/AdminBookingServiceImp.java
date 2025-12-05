package com.jamuara.crs.admin;


import com.jamuara.crs.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class AdminBookingServiceImp implements AdminBookingService{


    @Autowired
    private AdminBookingRepository  adminBookingRepository;

    @Override
    public List<Reservation> today(Reservation.BookingStatus status) {
        return adminBookingRepository.findToday(status.name());
    }

    @Override
    public List<Reservation> weekly(Reservation.BookingStatus status) {
        return adminBookingRepository.findThisWeek(status.name());
    }

    @Override
    public List<Reservation> monthly(Reservation.BookingStatus status) {
        return adminBookingRepository.findThisMonth(status.name());
    }
}

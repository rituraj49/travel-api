package com.jamuara.crs.admin;

import com.jamuara.crs.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class AdminBookingController {



    @Autowired
    private AdminBookingService bookingService;

    //  TODAY
    @GetMapping("/today")
    public List<Reservation> today(@RequestParam Reservation.BookingStatus status) {
        return bookingService.today(status);
    }

    //  WEEK
    @GetMapping("/week")
    public List<Reservation> week(@RequestParam Reservation.BookingStatus status) {
        return bookingService.weekly(status);
    }

    //  MONTH
    @GetMapping("/month")
    public List<Reservation> month(@RequestParam Reservation.BookingStatus status) {
        return bookingService.monthly(status);
    }
}

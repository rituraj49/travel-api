package com.jamuara.crs.flight.controller;

import com.jamuara.crs.flight.dto.tbo.ReservationStatusUpdateDto;
import com.jamuara.crs.flight.service.TboFlightService;
import com.jamuara.crs.model.Reservation;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tbo/admin")
public class TboAdminController {
    @Autowired
    private TboFlightService tboFlightService;

    @GetMapping("/bookings")
    public ResponseEntity<?> fetchAllBookings(@QueryParam("status") String status) {
        List<Reservation> reservations = new ArrayList<>();

        if(status != null) {
            Reservation.BookingStatus statusEnum = null;
            statusEnum = Reservation.BookingStatus.values()[Integer.parseInt(status) - 1];
            reservations = tboFlightService.getAllBookings(statusEnum);
        } else {
            reservations = tboFlightService.getAllBookings();
        }

        return ResponseEntity.ok().body(reservations);
    }

    @PutMapping("/update-reservation")
    public ResponseEntity<?> updateReservationStatus(@RequestBody ReservationStatusUpdateDto dto) {
        try {
            Reservation.BookingStatus status = Reservation.BookingStatus.values()[dto.getStatus() - 1];
            tboFlightService.updateBookingStatus(dto.getBookingId(), status);
            return ResponseEntity.status(HttpStatus.OK).body("status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("status update failed: " + e.getMessage());
        }
    }
}
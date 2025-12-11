package com.jamuara.crs.admin;

import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.flight.dto.tbo.ReservationStatusUpdateDto;
import com.jamuara.crs.flight.service.TboFlightService;
import com.jamuara.crs.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tbo/admin")
public class TboAdminController {
    @Autowired
    private TboFlightService tboFlightService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/bookings")
    public ResponseEntity<?> fetchAllBookings(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0" , required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size
    ) {
        Page<Reservation> reservationsPage;

        if (status != null) {
            Reservation.BookingStatus statusEnum =
                    Reservation.BookingStatus.values()[Integer.parseInt(status) - 1];

            reservationsPage = reservationService.findReservationsByStatus(statusEnum, page, size);
        } else {
            reservationsPage = reservationService.findAllReservations(page, size);
        }

        // Build response map
        Map<String, Object> response = new HashMap<>();
        response.put("content", reservationsPage.getContent());
        response.put("pageNumber", reservationsPage.getNumber());
        response.put("pageSize", reservationsPage.getSize());
        response.put("totalElements", reservationsPage.getTotalElements());
        response.put("totalPages", reservationsPage.getTotalPages());
        response.put("isLastPage", reservationsPage.isLast());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/bookings/update-status")
    public ResponseEntity<?> updateReservationStatus(@RequestBody ReservationStatusUpdateDto dto) {
        try {
//            Reservation.BookingStatus status = Reservation.BookingStatus.values()[dto.getStatus() - 1];
            tboFlightService.updateBookingStatus(dto.getBookingId(), Reservation.BookingStatus.valueOf(dto.getStatus()));
            return ResponseEntity.status(HttpStatus.OK).body("status updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("status update failed: " + e.getMessage());
        }
    }


    @GetMapping("/filter")
    public ResponseEntity<?> filterReservations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam Reservation.BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending()
        );

        Page<Reservation> reservationsPage =
                tboFlightService.filterReservations(from, to, status, pageable);

        //  Build response map
        Map<String, Object> response = new HashMap<>();
        response.put("content", reservationsPage.getContent());
        response.put("pageNumber", reservationsPage.getNumber());
        response.put("pageSize", reservationsPage.getSize());
        response.put("totalElements", reservationsPage.getTotalElements());
        response.put("totalPages", reservationsPage.getTotalPages());
        response.put("isLastPage", reservationsPage.isLast());

        return ResponseEntity.ok(response);
    }
}
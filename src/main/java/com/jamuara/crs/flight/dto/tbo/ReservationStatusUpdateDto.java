package com.jamuara.crs.flight.dto.tbo;

import com.jamuara.crs.model.Reservation;
import lombok.Data;

@Data
public class ReservationStatusUpdateDto {
    private String bookingId;

    private String status;
}

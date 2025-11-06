package com.jamuara.crs.model;

import com.jamuara.crs.enums.BookingStatusDb;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FlightBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String pnr;

    private String bookingId;

    @Enumerated(EnumType.STRING)
    private BookingStatusDb bookingStatus;

    private boolean isLcc;

    private boolean domestic;
}

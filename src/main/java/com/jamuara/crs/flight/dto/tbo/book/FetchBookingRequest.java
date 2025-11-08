package com.jamuara.crs.flight.dto.tbo.book;

import lombok.Data;

@Data
public class FetchBookingRequest {

    private String pnr;

    private String bookingId;

    private String firstName;

    private String lastName;
}

package com.jamuara.crs.flight.dto.tbo.book;

import lombok.Data;

@Data
public class FlightTicketRequestNonLcc {
    private String traceId;
    private String pnr;
    private String bookingId;
}

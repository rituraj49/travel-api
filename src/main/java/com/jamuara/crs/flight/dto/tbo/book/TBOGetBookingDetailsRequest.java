package com.jamuara.crs.flight.dto.tbo.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TBOGetBookingDetailsRequest {


    @JsonProperty("PNR")
    private String pnr;

    @JsonProperty("BookingId")
    private String bookingId;
}

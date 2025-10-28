package com.jamuara.crs.flight.dto.tbo.book;

import lombok.Data;

import java.util.List;

@Data
public class FlightBookingTicketingRequest {
    private String traceId;

    private String resultIndexOutbound;

    private String resultIndexInbound;

    private List<TravelerRequestDto> travelers;
}

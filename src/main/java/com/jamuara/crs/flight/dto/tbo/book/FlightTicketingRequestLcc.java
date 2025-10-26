package com.jamuara.crs.flight.dto.tbo.book;

import lombok.Data;

import java.util.List;

@Data
public class FlightTicketingRequestLcc {
    private String traceId;

    private String resultIndex;

    private List<TravelerRequestDto> travelers;
}

package com.jamuara.crs.flight.dto.tbo;

import lombok.Data;

@Data
public class FlightFareQuoteRequest {
    private String traceId;
    private String resultIndex;
}

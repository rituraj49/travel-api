package com.jamuara.crs.flight.dto.tbo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlightFareQuoteResponse {

    private String traceId;

    private String flightDetailChangeInfo;

    private boolean priceChanged;
    private String[] itineraryChangeList;

    private Map<String, List<FlightFareQuoteDetailsResponse>> flightsAvailable;
}

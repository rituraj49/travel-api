package com.jamuara.crs.flight.dto.tbo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlightSearchResponse {
    private String traceId; @Schema(example = "false")

    private String from;

    private String to;

    private Map<String, List<FlightDetailsResponse>> flightsAvailable;

//    private List<FlightDetailsResponse> outboundFlights;
//
//    private List<FlightDetailsResponse> inboundFlights;

}

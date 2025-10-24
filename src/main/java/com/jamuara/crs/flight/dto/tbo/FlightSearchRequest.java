package com.jamuara.crs.flight.dto.tbo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamuara.crs.enums.TripType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlightSearchRequest {

    private String originLocationCode;
    private String destinationLocationCode;
    private String departureDate;

//    @JsonIgnore
    private String returnDate;

    @Schema(example = "1")
    private int adults;

    @Schema(example = "1")
    private int children; // < 12 yr

    @Schema(example = "1")
    private int infants; // < 2 yr

    private boolean direct;

    private boolean oneStop;

//    private List<FlightSearchRequest.TripDetailsDto> tripDetails;

    @Schema(example = "ECONOMY")
    private FlightSearchRequest.Cabin travelClass;

//    private TripType tripType;

    @Data
    public static class TripDetailsDto {

        @Schema(example = "DEL")
        private String from;

        @Schema(example = "DEL")
        private String to;

        @Schema(example = "2025-01-01")
        private String departureDate;

        @Schema(example = "10:00:00")
        private String departureTime;
    }

    public static enum Cabin {
        ALL,
        ECONOMY,
        PREMIUM_ECONOMY,
        BUSINESS,
        PREMIUM_BUSINESS,
        FIRST
    }
}

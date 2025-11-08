package com.jamuara.crs.flight.dto.tbo.search;

import com.jamuara.crs.enums.TravelClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FlightSearchMulticityRequest {
    private List<TripDetailsDto> tripDetails;

    @Schema(example = "1")
    private int adults;

    @Schema(example = "1")
    private int children; // < 12 yr

    @Schema(example = "1")
    private int infants; // < 2 yr
//    private boolean isOneWay;

    private boolean oneStop;

    private boolean direct;

    @Data
    public static class TripDetailsDto {
        @Schema(example = "DEL")
        private String originLocationCode; // IATA code

        @Schema(example = "DEL")
        private String destinationLocationCode; // IATA code

        @Schema(example = "ECONOMY")
        private TravelClass cabin;

        @Schema(example = "2025-01-01")
        private String departureDate;

        @Schema(example = "10:00:00")
        private String departureTime;
    }
}

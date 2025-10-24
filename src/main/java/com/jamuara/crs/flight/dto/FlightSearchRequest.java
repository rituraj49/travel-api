package com.jamuara.crs.flight.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamuara.crs.enums.TravelClass;
import lombok.Data;

@Data
public class FlightSearchRequest {
    private String originLocationCode;
    private String destinationLocationCode;
    private String departureDate;

    @JsonIgnore
    private String returnDate;

    private int adults;
    private int children;
    private int infants;
    private TravelClass travelClass;
    private String currencyCode;
    private int max;
}

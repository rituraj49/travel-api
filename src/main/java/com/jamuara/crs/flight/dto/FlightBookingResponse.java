package com.jamuara.crs.flight.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlightBookingResponse {
    private String orderId;
    private List<TravelerRequestDto> travelers;
    private FlightAvailabilityResponse flightOffer;
}
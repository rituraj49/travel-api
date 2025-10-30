package com.jamuara.crs.flight.dto.tbo.book;

import lombok.Data;

import java.util.List;

@Data
public class FlightBookingTicketingCombinedResponse {
    private List<FlightBookingResponseNonLcc> bookingResponses;

    private List<FlightTicketResponse> ticketResponses;
}
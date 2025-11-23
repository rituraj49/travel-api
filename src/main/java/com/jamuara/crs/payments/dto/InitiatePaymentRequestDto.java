package com.jamuara.crs.payments.dto;

import com.jamuara.crs.flight.dto.tbo.book.FlightBookingTicketingRequest;
import lombok.Data;

@Data
public class InitiatePaymentRequestDto {
    private String amount;

    private String firstName;

    private String email;

    private String phone;

    private FlightBookingTicketingRequest bookingTicketingRequest;
}
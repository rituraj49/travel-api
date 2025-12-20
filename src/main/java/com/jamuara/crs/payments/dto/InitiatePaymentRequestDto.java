package com.jamuara.crs.payments.dto;

import com.jamuara.crs.enums.BookingType;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingTicketingRequest;
import lombok.Data;

import java.util.Map;

@Data
public class InitiatePaymentRequestDto {
    private String amount;

    private String firstName;

    private String email;

    private String phone;

    private BookingType bookingType;

    private Map<String, Object> bookingRequest;
}
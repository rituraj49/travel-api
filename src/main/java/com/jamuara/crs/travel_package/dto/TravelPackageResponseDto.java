package com.jamuara.crs.travel_package.dto;

import com.amadeus.resources.HotelOrder;
import com.jamuara.crs.activities.dto.ActivityResponse;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.model.HotelReservation;
import lombok.Data;

@Data
public class TravelPackageResponseDto {
    private FlightBookingResponse flightBooking;

    private HotelReservation hotelBooking;

    private ActivityResponse activityResponse;
}

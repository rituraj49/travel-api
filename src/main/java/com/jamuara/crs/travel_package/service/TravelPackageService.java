package com.jamuara.crs.travel_package.service;

import com.amadeus.resources.HotelOrder;
import com.jamuara.crs.flight.dto.FlightBookingRequest;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.flight.dto.TravelerRequestDto;
import com.jamuara.crs.flight.service.AmadeusFlightService;
import com.jamuara.crs.flight.service.IFlightService;
import com.jamuara.crs.hotel.service.HotelService;
import com.jamuara.crs.hotel.service.IHotelService;
import com.jamuara.crs.model.HotelReservation;
import com.jamuara.crs.travel_package.dto.TravelPackageRequestDto;
import com.jamuara.crs.travel_package.dto.TravelPackageResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TravelPackageService {
    @Autowired
    private IFlightService flightService;

    @Autowired
    private IHotelService hotelService;

    public TravelPackageResponseDto travelPackageBooking(TravelPackageRequestDto dto) throws Exception {
        TravelPackageResponseDto packageResponse = new TravelPackageResponseDto();

        if(dto.hasFlights) {
            FlightBookingRequest flightBookingRequest = new FlightBookingRequest();
            flightBookingRequest.setFlightOffer(dto.getFlightOffer());
            flightBookingRequest.setTravelers(dto.getTravelers());

            FlightBookingResponse flightBooking = flightService.createFlightOrder(flightBookingRequest);
            packageResponse.setFlightBooking(flightBooking);
        }

        if(dto.hasHotels) {
            HotelReservation hotelOrder = hotelService.bookHotel(dto.getHotel());
            packageResponse.setHotelBooking(hotelOrder);
        }

        return packageResponse;
    }
}

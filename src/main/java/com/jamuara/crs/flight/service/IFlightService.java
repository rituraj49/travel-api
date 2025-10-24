package com.jamuara.crs.flight.service;

import com.jamuara.crs.flight.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IFlightService {
    List<FlightAvailabilityResponse> searchFlights(FlightSearchRequest flightSearchRequest) throws Exception;
    List<FlightAvailabilityResponse> searchMultiCityFlights(FlightAvailabilityRequest flightOfferSearchRequestDto) throws Exception;
    FlightPricingResponse confirmFlightPrice(String flightOffer) throws Exception;
    FlightBookingResponse createFlightOrder(FlightBookingRequest flightOrderRequest) throws Exception;
    FlightBookingResponse fetchFlightOrder(String orderId) throws Exception;
}

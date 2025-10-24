package com.jamuara.crs.flight.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.flight.dto.*;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

//@Service("amadeusOfflineService")
@Service
@Profile("offline")
public class AmadeusOfflineService implements IFlightService {

    ObjectMapper mapper = new ObjectMapper();

    private String readFile(String fileName) throws Exception {
        File file = new ClassPathResource(fileName).getFile();
        return Files.readString(file.toPath());
    }

    @Override
    public List<FlightAvailabilityResponse> searchFlights(FlightSearchRequest flightSearchRequest) {
        try {
            String json = readFile("data/flight_search_response.text");
//            System.out.println(json);
            List<FlightAvailabilityResponse> result = mapper.readValue(json, new TypeReference<List<FlightAvailabilityResponse>>() {});
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline flight search", e);
        }
    }

    public List<FlightAvailabilityResponse> searchMultiCityFlights(FlightSearchRequest flightSearchRequest) {
        try {
            String json = readFile("data/multicity_flight_search_response.text");
            List<FlightAvailabilityResponse> result = mapper.readValue(json, new TypeReference<List<FlightAvailabilityResponse>>() {});
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline multicity search", e);
        }
    }

    @Override
    public FlightPricingResponse confirmFlightPrice(String flightOffer) {
        try {
            String json=readFile("data/flight_pricing_confirmation_response.text");
            return mapper.readValue(json, new TypeReference<FlightPricingResponse>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline multicity search",e);
        }
    }

    @Override
    public FlightBookingResponse createFlightOrder(FlightBookingRequest flightOrderRequest) {
        try{
            String json=readFile("data/booking_confirm_response.text");
            return mapper.readValue(json, new TypeReference<FlightBookingResponse>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline multicity search",e);
        }
    }

    @Override
    public List<FlightAvailabilityResponse> searchMultiCityFlights(FlightAvailabilityRequest flightOfferSearchRequestDto)
    {
        String json= null;
        try {
            json = readFile("data/multi_city_flight_search_response.text");
            return mapper.readValue(json, new TypeReference<List<FlightAvailabilityResponse>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error reading offline multicity search",e);
        }

    }

    @Override
    public FlightBookingResponse fetchFlightOrder(String orderId) throws Exception {
        return null;
    }
}

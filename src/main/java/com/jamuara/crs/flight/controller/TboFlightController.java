package com.jamuara.crs.flight.controller;

import com.amadeus.exceptions.ResponseException;
import com.jamuara.crs.flight.dto.FlightAvailabilityResponse;
import com.jamuara.crs.flight.dto.tbo.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.FlightSearchResponse;
import com.jamuara.crs.flight.service.TboFlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tbo/flights")
@Slf4j
public class TboFlightController {
    TboFlightService tboFlightService;

    public  TboFlightController(TboFlightService tboFlightService) {
        this.tboFlightService = tboFlightService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(@ModelAttribute FlightSearchRequest request) {
        try {
            FlightSearchResponse response = tboFlightService.flightSearch(request);
//            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            log.error("An internal error occurred while processing flight offer search API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

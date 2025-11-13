package com.jamuara.crs.flight.controller;

import com.jamuara.crs.flight.dto.tbo.FlightFareRuleResponse;
import com.jamuara.crs.flight.dto.tbo.FlightFareRulesCumQuoteRequest;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteResponse;
import com.jamuara.crs.flight.dto.tbo.book.*;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchMulticityRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchResponse;
import com.jamuara.crs.flight.service.TboFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping("/search")
    public ResponseEntity<?> searchFlightsMulticity(@RequestBody FlightSearchMulticityRequest request) {
        try {
            FlightSearchResponse response = tboFlightService.flightMulticitySearch(request);
//            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An internal error occurred while processing flight offer search API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Fetch fare rules for selected flight(s)",
            description = "Retrieves fare rules details (fare rules details for each leg) for the selected flight combinations using trace ID and result indices."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fare rules retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/fare-rules")
    public ResponseEntity<?> fetchFareRules(@io.swagger.v3.oas.annotations.parameters.RequestBody(
                                               required = true,
                                               description = "Flight fare rule request details",
                                               content = @Content(
                                                       schema = @Schema(implementation = FlightFareRulesCumQuoteRequest.class),
                                                       examples = @ExampleObject(
                                                               name = "Sample Request",
                                                               value = """
                                                    {
                                                      "traceId": "a1b2c3d4-e5f6-7890-gh12-i345j678k901",
                                                      "resultIndexOutbound": "OB123456",
                                                      "resultIndexInbound": "IB654321"
                                                    }
                                                    """
                                                       )
                                               )
                                       )
                                       @RequestBody FlightFareRulesCumQuoteRequest request
    ) {
        try {
            List<Map<String, FlightFareRuleResponse>> response = tboFlightService.flightFareRules(request);
//            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            log.error("An internal error occurred while processing flight offer search API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Fetch fare quote for selected flight(s)",
            description = "Retrieves fare quote details (price, availability, and booking conditions) for the selected flight combinations using trace ID and result indices."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fare quote retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/fare-quote")
    public ResponseEntity<?> fareQuote(@io.swagger.v3.oas.annotations.parameters.RequestBody(
                                       required = true,
                                       description = "Flight fare quote request details",
                                       content = @Content(
                                       schema = @Schema(implementation = FlightFareRulesCumQuoteRequest.class),
                                       examples = @ExampleObject(
                                            name = "Sample Request",
                                            value = """
                                                    {
                                                      "traceId": "a1b2c3d4-e5f6-7890-gh12-i345j678k901",
                                                      "resultIndexOutbound": "OB123456",
                                                      "resultIndexInbound": "IB654321"
                                                    }
                                                    """
                                       )
                                 )
                            )
            @RequestBody FlightFareRulesCumQuoteRequest request
    ) {
        try {
            List<Map<String, FlightFareQuoteResponse>> response = tboFlightService.flightFareQuote(request);
//            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            log.error("An internal error occurred while processing flight offer search API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Book selected flight(s)",
            description = """
                Confirms a flight booking using the provided trace ID, selected flight result indices, and traveler details.
                This endpoint should be called after fare quote confirmation. Booked for non-lcc flights and ticket generated for lcc flights
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight booked successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/book")
    public ResponseEntity<?> flightBooking(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Flight booking request details including traveler and flight information",
                    content = @Content(
                            schema = @Schema(implementation = FlightBookingTicketingRequest.class),
                            examples = @ExampleObject(
                                    name = "Sample Booking Request",
                                    value = """
                            {
                                "traceId": "ebe6e3bf-55db-43cf-81a3-5f3c063c0f87",
                                "resultIndexOutbound": "OB6[TBO]YU8",
                                "resultIndexInbound": "IB1[TBO]JIU",
                                "travelers": [
                                    {
                                        "title": "Mr",
                                        "firstName": "Rahul",
                                        "lastName": "Sharma",
                                        "dateOfBirth": "1992-02-09",
                                        "email": "rahul@test.com",
                                        "gender": "MALE",
                                        "travelerType": "ADULT",
                                        "lead": true,
                                        "phoneCountryCode": "+91",
                                        "phone": "9876543210",
                                        "passportDetails": {
                                            "number": "AB1234567",
                                            "expiryDate": "2026-03-01",
                                            "nationality": "IN"
                                        },
                                        "address": {
                                            "line1": "123, Street Name",
                                            "line2": "Apartment 456",
                                            "city": "Mumbai",
                                            "country": "India",
                                            "countryCode": "IN"
                                        }
                                    }
                                ]
                            }
                            """
                                    )
                            )
                    )
            @RequestBody FlightBookingTicketingRequest request
    ) {
        try {
            List<FetchFlightBookingResponse> response = tboFlightService.flightBookAndTicket(request);
//            log.info("{} flight offers found", flightResponseList.size());

            // Save booking details for each response
//            for (FetchFlightBookingResponse response1 : response) {
//                tboFlightService.saveBookingDetails(response1);
//            }

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("An internal error occurred while processing flight offer search API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Fetch booking details for a given PNR and Booking ID",
            description = "Retrieves detailed flight booking information (itinerary, passengers, fare, etc.) from the TBO API using the provided PNR and Booking ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking details retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/bookings/fetch")
    public ResponseEntity<?> getBookingDetails(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Booking details request containing PNR and Booking ID",
                    content = @Content(
                            schema = @Schema(implementation = FetchBookingRequest.class),
                            examples = @ExampleObject(
                                    name = "Sample Request",
                                    value = """
                                        {
                                          "pnr": "ABC123",
                                          "bookingId": "987654321"
                                        }
                                        """
                            )
                    )
            )
            @RequestBody FetchBookingRequest request
    ) {
        try {
            FetchFlightBookingResponse response = tboFlightService.fetchBookingDetails(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            log.error("Error while fetching booking details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}

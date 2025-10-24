package com.jamuara.crs.flight.controller;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.google.gson.Gson;
import com.jamuara.crs.flight.dto.*;
import com.jamuara.crs.flight.service.IFlightService;
import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.common.service.UserLogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("flights")
@Tag(name = "flight search controller")
@Slf4j
public class FlightController {

    private final IFlightService flightService;

    private final Gson gson = new Gson();

    @Autowired
    private Amadeus amadeusClient;

//    public FlightController(@Qualifier("amadeusOfflineService") IFlightService flightService) {
    public FlightController(IFlightService flightService) {
        this.flightService = flightService;
    }

    @ApiResponse(responseCode = "200", description = " return all available flight  ")
    @Operation(
            summary = "find flight offer search",
            description = " Example Payload:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"originLocationCode\": \"SYD\",\n" +
                    "  \"destinationLocationCode\": \"NYC\",\n" +
                    "  \"departureDate\": \"2025-12-31\",\n" +
                    "  \"returnDate\": \"2026-01-01\",\n" +
                    "  \"maxPrice\": 140000,\n" +
                    "  \"adults\": 1,\n" +
                    "  \"children\":0, \n"+
                    "  \"infants\":0, \n"+
                    "  \"travelClass\": \"ECONOMY\", \n"+
                    "  \"nonStop\": \"false\", \n"+
                    "  \"currencyCode\": \"INR\" ,\n" +
                    "  \"max\": 5\n" +
                    "}\n" +
                    "```"+" max-> show only 5 result \n  if you want to Excluded any Airline than use \"excludedAirlineCodes\":\"AI\" \n or if you want to Included Airlines than use \"includedAirlineCodes\":\"AI\" \n     "
    )

    @GetMapping("/search")
    public ResponseEntity<?> flightOfferSearch(FlightSearchRequest queryParams) {
        log.info("flight offer search params received: {}", queryParams.toString());
//        FlightOfferSearch[] flightOffers = flightService.flightSearch(queryParams);

        try {
            List<FlightAvailabilityResponse> flightResponseList = flightService.searchFlights(queryParams);
            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(flightResponseList);
        } catch (ResponseException e) {
            log.error("An Error occurred while processing flight offer search API: {}", e.getDescription());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("An internal error occurred while processing flight offer search API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Find multi-city flight offer search",
            description = "Example Payload:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"currencyCode\": \"INR\",\n" +
                    "  \"tripDetails\": [\n" +
                    "    {\n" +
                    "      \"id\": \"1\",\n" +
                    "      \"from\": \"BKK\",\n" +
                    "      \"to\": \"BLR\",\n" +
                    "      \"departureDate\": \"2025-12-28\",\n" +
                    "      \"departureTime\": \"10:00:00\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"2\",\n" +
                    "      \"from\": \"BLR\",\n" +
                    "      \"to\": \"BOM\",\n" +
                    "      \"departureDate\": \"2025-12-30\",\n" +
                    "      \"departureTime\": \"10:00:00\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"adults\": 1,\n" +
                    "  \"children\": 0,\n" +
                    "  \"infants\": 0,\n" +
                    "  \"maxCount\": 2,\n" +
                    "  \"cabin\": \"ECONOMY\"\n" +
                    "}\n" +
                    "```"
    )
    @ApiResponse(responseCode = "200", description = " return all available flight",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightAvailabilityResponse.class)))
    @PostMapping("/search")
    public ResponseEntity<?> flightOfferMultiCitySearch(@RequestBody FlightAvailabilityRequest flightRequestDto) {
        try {
            log.info("multicity search flight offer request received: {}", flightRequestDto.toString());
//            FlightOfferSearch[] flightOffers = flightService.flightMultiCitySearch(flightRequestDto);
            List<FlightAvailabilityResponse> flightResponseList = flightService.searchMultiCityFlights(flightRequestDto);

//            log.info("{} flight offers found", flightResponseList.size());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(flightResponseList);
        }
        catch (Exception e) {
            log.error("An Error occurred while processing multi city search offer API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Find the price of a flight offer search",
            description = "Step 1: First, search for flights.\n" +
                    "Step 2: Copy the 'pricingAdditionalInfo' value from the flight offer search API response.\n" +
                    "Step 3: Paste it as the 'flightOffer' value in this API."
    )
    @ApiResponse(responseCode = "200", description = " return all available flight",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightPricingResponse.class)))
    @PostMapping("price")
    public ResponseEntity<?> flightOfferPrice(@RequestBody FlightPricingRequest flightRequest) {
        try {
            log.info("flight offer pricing confirmation request received");

            FlightPricingResponse response = flightService.confirmFlightPrice(flightRequest.getFlightOffer());

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("An Error occurred while processing pricing flight offer search offer API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Book flight and create flight booking order using Amadeus API",
            description = "Create a flight booking order using the Amadeus API.\n\n" +
                    "The request body should contain:\n" +
                    "- A FlightOffer object in an array\n" +
                    "- Travelers details in the travelers array in JSON format\n\n" +
                    "Example Travelers Documents Payload:\n" +
                    "```json\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"documentType\": \"PASSPORT\",\n" +
                    "    \"number\": \"M1234567\",\n" +
                    "    \"birthPlace\": \"Delhi\",\n" +
                    "    \"issuanceLocation\": \"Delhi\",\n" +
                    "    \"issuanceCountry\": \"IN\",\n" +
                    "    \"issuanceDate\": \"2016-03-10\",\n" +
                    "    \"expiryDate\": \"2026-03-10\",\n" +
                    "    \"validityCountry\": \"IN\",\n" +
                    "    \"nationality\": \"IN\",\n" +
                    "    \"holder\": true\n" +
                    "  }\n" +
                    "]\n" +
                    "```\n\n" +
                    "Steps:\n" +
                    "1. Copy `bookingAdditionalInfo` value from the pricing API.\n" +
                    "2. Paste it as the `flightOffer` value.\n" +
                    "3. Provide travelers documents as shown above."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Flight order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightBookingResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error while creating flight order"),
    })
    @PostMapping("flight-order")
    public ResponseEntity<?> flightOrderCreate(@RequestBody FlightBookingRequest orderRequest) {
        log.info("flight booking request received");
        try {
            FlightBookingResponse createdOrder = flightService.createFlightOrder(orderRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            log.error("Error creating flight order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Get flight order by ID",
            description = "Fetch a flight booking order using the Amadeus API by providing the order ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Flight order retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightBookingResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Flight order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error while fetching flight order")
    })
    @GetMapping("flight-order/{orderId}")
    public ResponseEntity<?> getFlightBooking(@PathVariable String orderId) {
        log.info("Fetching flight order with ID: {}", orderId);
        try {
            FlightBookingResponse flightOrder = flightService.fetchFlightOrder(orderId);
            if (flightOrder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight order not found");
            }
            log.info("Flight order with ID: {} retrieved successfully", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(flightOrder);
        } catch (Exception e) {
            log.error("Error fetching flight order with ID: {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

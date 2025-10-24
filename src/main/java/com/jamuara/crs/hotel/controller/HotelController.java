package com.jamuara.crs.hotel.controller;

import com.amadeus.exceptions.ResponseException;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import com.jamuara.crs.hotel.service.HotelService;
import com.jamuara.crs.hotel.service.IHotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("hotels")
@Slf4j
public class HotelController {

    private IHotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/search")
    @Operation(summary = "Search Hotels by City",
            description = "Search hotels using cityCode (mandatory), radius, radiusUnit, amenities, and ratings.")
    public ResponseEntity<?> searchHotels(
            @RequestParam
            @Parameter(description = "Destination city code (IATA 3-letter)", required = true, example = "PAR")
            String cityCode,

            @RequestParam(required = false)
            @Parameter(description = "Maximum distance from city center", example = "5")
            Integer radius,

            @RequestParam(required = false)
            @Parameter(description = "Radius unit (KM or MI)", example = "KM")
            String radiusUnit,

            @RequestParam(required = false)
            @Parameter(description = "List of amenities  like SWIMMING_POOL , \n" +
                    "FITNESS_CENTER, \n" +
                    "AIR_CONDITIONING, \n" +
                    "RESTAURANT, \n" +
                    "PARKING, \n" +
                    "PETS_ALLOWED, \n" +
                    "AIRPORT_SHUTTLE, \n" +
                    "BUSINESS_CENTER, \n" +
                    "DISABLED_FACILITIES, \n" +
                    "WIFI,", example = "[\"SWIMMING_POOL\", \"SPA\"]")
            List<String> amenities,

            @RequestParam(required = false)
            @Parameter(description = "Hotel star ratings (1–5)")//, example = "[\"3\", \"4\"]")
            List<String> ratings
    ) {
        try {
            List<HotelSearchResponse> hotelsList = hotelService.getHotels(cityCode, radius, radiusUnit, amenities, ratings);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(hotelsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong: " + e.getMessage());
        }
    }

    @GetMapping("/search/offers")
    @Operation(summary = "Get Hotel Offers", description = "Search hotel offers by hotelIds, dates, guests, and filters.")
    public ResponseEntity<?> getHotelOffers(
            @RequestParam
            @Parameter(description = "Amadeus property codes (8 chars). Mandatory when searching by predefined list of hotels.", required = true, example = "PAR12345")
            List<String> hotelIds,

            @RequestParam(required = false)
            @Parameter(description = "Number of adult guests (1-9) per room", example = "2")
            Integer adults,

            @RequestParam(required = false)
            @Parameter(description = "Check-in date (YYYY-MM-DD)", example = "2025-09-01")
            String checkInDate,

            @RequestParam(required = false)
            @Parameter(description = "Check-out date (YYYY-MM-DD)", example = "2025-09-02")
            String checkOutDate,

            @RequestParam(required = false)
            @Parameter(description = "Number of rooms requested (1-9)", example = "1")
            Integer roomQuantity,

            @RequestParam(required = false)
            @Parameter(description = "Return only the cheapest offer per hotel if true", example = "true")
            Boolean bestRateOnly
    ) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("hotelIds", String.join(",", hotelIds));
            if (adults != null) params.put("adults", adults.toString());
            if (checkInDate != null) params.put("checkInDate", checkInDate);
            if (checkOutDate != null) params.put("checkOutDate", checkOutDate);
            if (roomQuantity != null) params.put("roomQuantity", roomQuantity.toString());
            if (bestRateOnly != null) params.put("bestRateOnly", bestRateOnly.toString());

            return ResponseEntity.ok(hotelService.getOffers(params));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookHotelV2(@RequestBody String body) {
        try {
            String response = hotelService.bookHotel(body);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (ResponseException e) {
//            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getDescription());
        } catch (Exception e) {
//            e.printStackTrace();
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }
}

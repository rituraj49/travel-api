package com.jamuara.crs.common.location.controller;

import com.amadeus.exceptions.ResponseException;
import com.jamuara.crs.common.location.dto.LocationResponse;
import com.jamuara.crs.common.location.service.ISearchService;
import com.jamuara.crs.es.ESService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@Slf4j
@Tag(name = "Location Search Controller")
public class LocationSearchController {
    private final ISearchService searchService;

    private final ESService esService;

//    public LocationSearchController(@Qualifier("amadeusLocationService")ISearchService searchService, ESService esService) {
//    public LocationSearchController(@Qualifier("amadeusOfflineService") ISearchService searchService, ESService esService) {
    public LocationSearchController(ISearchService searchService, ESService esService) {
        this.searchService = searchService;
        this.esService = esService;
    }

    @Operation(summary = "Search for airports using in-memory Lucene index",
            description = """
					Search for airports using an in-memory Lucene index. The query parameter 'keyword' should be provided. payload: new york
					""")
    @ApiResponse(responseCode = "200", description = "Search for airports using in-memory Lucene index")
    @Parameter(name = "keyword", description = "Query string for searching airports", required = true)
    @GetMapping("search")
    public ResponseEntity<?> searchAirports(@RequestParam String keyword) throws Exception {
        log.info("start location search: {}", keyword);
        List<LocationResponse> airportResponses = searchService.keywordSearch(keyword);

        if(airportResponses.isEmpty()) {
            log.warn("no locations found for the given keyword: {}", keyword);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No locations found for the given keyword");
        }
        log.info("found {} locations for the keyword: {}", airportResponses.size(), keyword);
        return ResponseEntity.status(HttpStatus.OK).body(airportResponses);
    }

    @Operation(
            summary = "Find airport or city location by keyword",
            description = "Example payloads:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"keyword\": \"BOM\",\n" +
                    "  \"subType\": \"CITY,AIRPORT\"\n" +
                    "}\n" +
                    "\n" +
                    "{\n" +
                    "  \"keyword\": \"JFK\",\n" +
                    "  \"subType\": \"AIRPORT\"\n" +
                    "}\n" +
                    "\n" +
                    "{\n" +
                    "  \"keyword\": \"HYD\",\n" +
                    "  \"subType\": \"CITY\"\n" +
                    "}\n" +
                    "```" +" you can use any one "
    )
    @ApiResponse(responseCode = "200", description = "Search for locations using Amadeus API")
    @Parameter(name = "params", description = "Query parameters in the form of keyword=\"nyc\" and subType=\"CITY,AIRPORT\" pairs for searching locations", required = true)
    @GetMapping("amadeus-search")
    public ResponseEntity<?> searchForLocations(@RequestParam String keyword) {
        try {
            log.info("keyword received in location search by amadeus: {}", keyword);
            List<LocationResponse> response = searchService.keywordSearch(keyword);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("es-search")
    public ResponseEntity<?> searchLocations(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            log.info("Received keyword for search: {}", keyword);
			List<LocationResponse> result = esService.searchByKeyword(keyword, page, size);
//            LocationResponseWrapper result = esService.searchByKeyword(keyword, page, size);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while searching locations: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
        }
    }
}

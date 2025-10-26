package com.jamuara.crs.flight.service;

import com.jamuara.crs.common.service.RestService;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteRequest;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteResponse;
import com.jamuara.crs.flight.dto.tbo.TboApiFareQuoteResponseDto;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingRequestNonLcc;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingResponseNonLcc;
import com.jamuara.crs.flight.dto.tbo.book.TboApiFlightBookingResponseDto;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchResponse;
import com.jamuara.crs.flight.dto.tbo.search.TboApiFlightResponseDto;
import com.jamuara.crs.flight.mapper.TboFlightBookingResponseMapper;
import com.jamuara.crs.flight.mapper.TboFlightFareQuoteResponseMapper;
import com.jamuara.crs.flight.mapper.TboFlightRequestMapper;
import com.jamuara.crs.flight.mapper.TboFlightSearchResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TboFlightService {
    RestService restService;

    TboFlightSearchResponseMapper tboFlightSearchResponseMapper;

    TboFlightBookingResponseMapper tboFlightBookingResponseMapper;

    TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper;

    private final String TBO_FLIGHT_URL = "http://api.tektravels.com/BookingEngineService_Air/AirService.svc/rest";

    public TboFlightService(
            RestService restService,
            TboFlightSearchResponseMapper tboFlightSearchResponseMapper,
            TboFlightBookingResponseMapper tboFlightBookingResponseMapper,
            TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper
    ) {
        this.restService = restService;
        this.tboFlightSearchResponseMapper = tboFlightSearchResponseMapper;
        this.tboFlightBookingResponseMapper = tboFlightBookingResponseMapper;
        this.tboFlightFareQuoteResponseMapper = tboFlightFareQuoteResponseMapper;
    }

    public FlightSearchResponse flightSearch(FlightSearchRequest searchRequest) {
        log.info("search request received: {}", searchRequest.toString());
        Map<String, Object> requestBody = TboFlightRequestMapper.mapDtoToFlightRequest(searchRequest);
        ResponseEntity<TboApiFlightResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Search",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightResponseDto>() {}
        );

        return tboFlightSearchResponseMapper.mapToFlightSearchResponse(response.getBody().getResponse());
    }

    public FlightFareQuoteResponse flightFareQuote(FlightFareQuoteRequest request) {
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFareQuoteRequest(request);
        ResponseEntity<TboApiFareQuoteResponseDto> responseBody = restService.sendRequest(
                TBO_FLIGHT_URL + "/FareQuote",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFareQuoteResponseDto>() {}
        );

//        FlightFareQuoteResponse response = tboFlightFareQuoteResponseMapper.mapToFlightFareQuoteResponse(responseBody.getBody().getResponse());
        //FlightFareQuoteResponse response = tboFareQuoteResponseMapper.mapToFareQuoteResponse(responseBody);
        FlightFareQuoteResponse response =  tboFlightFareQuoteResponseMapper.mapToFlightFareQuoteResponse(responseBody.getBody().getResponse());

        
        return response;
    }

    public FlightBookingResponseNonLcc flightBook(FlightBookingRequestNonLcc request) {
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToBookingRequest(request);

        ResponseEntity<TboApiFlightBookingResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Book",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightBookingResponseDto>() {}
        );

        System.out.println(response + " : book response:");

        return tboFlightBookingResponseMapper.mapToBookingResponse(response.getBody());
    }
}

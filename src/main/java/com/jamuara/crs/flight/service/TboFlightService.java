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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class TboFlightService {
    RestService restService;

    TboFlightSearchResponseMapper tboFlightSearchResponseMapper;

    TboFlightBookingResponseMapper tboFlightBookingResponseMapper;

    TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper;

    CacheManager cacheManager;

    private final String TBO_FLIGHT_URL = "http://api.tektravels.com/BookingEngineService_Air/AirService.svc/rest";

    public TboFlightService(
            RestService restService,
            TboFlightSearchResponseMapper tboFlightSearchResponseMapper,
            TboFlightBookingResponseMapper tboFlightBookingResponseMapper,
            TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper,
            CacheManager cacheManager
    ) {
        this.restService = restService;
        this.tboFlightSearchResponseMapper = tboFlightSearchResponseMapper;
        this.tboFlightBookingResponseMapper = tboFlightBookingResponseMapper;
        this.tboFlightFareQuoteResponseMapper = tboFlightFareQuoteResponseMapper;
        this.cacheManager = cacheManager;
    }

    public FlightSearchResponse flightSearch(FlightSearchRequest searchRequest) throws Exception {
        log.info("search request received: {}", searchRequest.toString());
        Map<String, Object> requestBody = TboFlightRequestMapper.mapDtoToFlightRequest(searchRequest);
        ResponseEntity<TboApiFlightResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Search",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightResponseDto>() {}
        );

        if(!Objects.equals(response.getBody().getResponse().getError().getErrorMessage(), "")) {
            throw new Exception(response.getBody().getResponse().getError().getErrorMessage());
        }
        return tboFlightSearchResponseMapper.mapToFlightSearchResponse(response.getBody().getResponse());
    }

    public FlightFareQuoteResponse flightFareQuote(FlightFareQuoteRequest request) throws Exception {

        log.info("flight fare quote request received");
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFareQuoteRequest(request);

        ResponseEntity<TboApiFareQuoteResponseDto> responseBody = restService.sendRequest(
                TBO_FLIGHT_URL + "/FareQuote",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFareQuoteResponseDto>() {}
        );

        if(!Objects.equals(responseBody.getBody().getResponse().getError().getErrorMessage(), "")) {
            throw new Exception(responseBody.getBody().getResponse().getError().getErrorMessage());
        }

//        FlightFareQuoteResponse response = tboFlightFareQuoteResponseMapper.mapToFlightFareQuoteResponse(responseBody.getBody().getResponse());
        //FlightFareQuoteResponse response = tboFareQuoteResponseMapper.mapToFareQuoteResponse(responseBody);
        FlightFareQuoteResponse response =  tboFlightFareQuoteResponseMapper.mapToFlightFareQuoteResponse(responseBody.getBody().getResponse());
        String traceId = response.getTraceId();
        var flightDetails = response.getFlightsAvailable()
                    .get("flight")
                    .get(0);

        cacheManager.getCache("fareQuote").put(traceId, flightDetails);

        return response;
    }

    public FlightBookingResponseNonLcc flightBook(FlightBookingRequestNonLcc request) throws Exception {
//        Cache cache = cacheManager.getCache("fareQuote");
//        Cache.ValueWrapper wrapper = cache.get(request.getTraceId());

        Map<String, Object> requestBody = TboFlightRequestMapper.mapToBookingRequest(request, cacheManager);
        log.info("flight book request body: {}", requestBody.toString());
        ResponseEntity<TboApiFlightBookingResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Book",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightBookingResponseDto>() {}
        );

        if(!Objects.equals(response.getBody().getError().getErrorMessage(), "")) {
            throw new Exception(response.getBody().getError().getErrorMessage());
        }

        System.out.println("response status: " + response.getBody().getResponse().getStatus() + " : book response:");

        return tboFlightBookingResponseMapper.mapToBookingResponse(response.getBody());
    }
}

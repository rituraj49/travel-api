package com.jamuara.crs.flight.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.common.service.RestService;
import com.jamuara.crs.common.service.TboAuthService;
import com.jamuara.crs.flight.dto.tbo.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.FlightSearchResponse;
import com.jamuara.crs.flight.dto.tbo.TboApiFlightResponseDto;
import com.jamuara.crs.flight.mapper.TboFlightSearchRequestMapper;
import com.jamuara.crs.flight.mapper.TboFlightSearchResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TboFlightService {
    RestService restService;

    TboFlightSearchResponseMapper tboFlightSearchResponseMapper;

    private final String TBO_FLIGHT_URL = "http://api.tektravels.com/BookingEngineService_Air/AirService.svc/rest";

    public TboFlightService(RestService restService, TboFlightSearchResponseMapper tboFlightSearchResponseMapper) {
        this.restService = restService;
        this.tboFlightSearchResponseMapper = tboFlightSearchResponseMapper;
    }

    public FlightSearchResponse flightSearch(FlightSearchRequest searchRequest) {
        log.info("search request received: {}", searchRequest.toString());
        Map<String, Object> requestBody = TboFlightSearchRequestMapper.mapDtoToFlightRequest(searchRequest);
        ResponseEntity<TboApiFlightResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Search",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightResponseDto>() {}
        );

        return tboFlightSearchResponseMapper.mapToFlightSearchResponse(response.getBody().getResponse());
    }
}

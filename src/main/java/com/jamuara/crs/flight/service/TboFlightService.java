package com.jamuara.crs.flight.service;

import com.jamuara.crs.common.service.RestService;
import com.jamuara.crs.common.service.TboAuthService;
import com.jamuara.crs.enums.BookingStatusDb;
import com.jamuara.crs.flight.dto.FlightBookingRequest;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.*;
import com.jamuara.crs.flight.dto.tbo.book.*;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchResponse;
import com.jamuara.crs.flight.dto.tbo.search.TboApiFlightResponseDto;
import com.jamuara.crs.flight.mapper.*;
import com.jamuara.crs.flight.repopsitory.FlightBookingRepository;
import com.jamuara.crs.model.FlightBooking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TboFlightService {
    RestService restService;

    TboFlightSearchResponseMapper tboFlightSearchResponseMapper;

    TboFlightBookingResponseMapper tboFlightBookingResponseMapper;

    TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper;

    TboFlightTicketMapper tboFlightTicketMapper;

    @Autowired(required = false)
    FlightBookingRepository flightBookingRepository;

    CacheManager cacheManager;

    private final String TBO_FLIGHT_URL = "http://api.tektravels.com/BookingEngineService_Air/AirService.svc/rest";

    public TboFlightService(
            RestService restService,
            TboFlightSearchResponseMapper tboFlightSearchResponseMapper,
            TboFlightBookingResponseMapper tboFlightBookingResponseMapper,
            TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper,
            TboFlightTicketMapper tboFlightTicketMapper,
            CacheManager cacheManager
    ) {
        this.restService = restService;
        this.tboFlightSearchResponseMapper = tboFlightSearchResponseMapper;
        this.tboFlightBookingResponseMapper = tboFlightBookingResponseMapper;
        this.tboFlightFareQuoteResponseMapper = tboFlightFareQuoteResponseMapper;
        this.tboFlightTicketMapper = tboFlightTicketMapper;
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

    public List<Map<String, FlightFareQuoteResponse>> flightFareQuote(FlightFareQuoteRequest request) throws Exception {

        log.info("flight fare quote request received");
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFareQuoteRequest(request);

        ResponseEntity<TboApiFareQuoteResponseDto> outboundResponse = restService.sendRequest(
                TBO_FLIGHT_URL + "/FareQuote",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody.get("outbound"),
                new ParameterizedTypeReference<TboApiFareQuoteResponseDto>() {}
        );

        if(!Objects.equals(outboundResponse.getBody().getResponse().getError().getErrorMessage(), "")) {
            throw new Exception(outboundResponse.getBody().getResponse().getError().getErrorMessage());
        }

        FlightFareQuoteResponse outboundFareQuote =  tboFlightFareQuoteResponseMapper.mapToFlightFareQuoteResponse(outboundResponse.getBody().getResponse());
        String traceId = outboundFareQuote.getTraceId();
        var outboundFlightDetails = outboundFareQuote.getFlightsAvailable()
                .get("flight")
                .get(0);

        ResponseEntity<TboApiFareQuoteResponseDto> inboundResponse = null;
        if(requestBody.containsKey("inbound")) {
            inboundResponse = restService.sendRequest(
                    TBO_FLIGHT_URL + "/FareQuote",
                    HttpMethod.POST,
                    new HashMap<>(),
                    requestBody.get("inbound"),
                    new ParameterizedTypeReference<TboApiFareQuoteResponseDto>() {}
            );

            if(!Objects.equals(inboundResponse.getBody().getResponse().getError().getErrorMessage(), "")) {
                throw new Exception(outboundResponse.getBody().getResponse().getError().getErrorMessage());
            }
        }

        Object inboundFlightDetails = null;
        FlightFareQuoteResponse inboundFareQuote = null;

        if(inboundResponse != null) {
            inboundFareQuote = tboFlightFareQuoteResponseMapper.mapToFlightFareQuoteResponse(inboundResponse.getBody().getResponse());
            inboundFlightDetails = inboundFareQuote.getFlightsAvailable()
                .get("flight")
                .get(0);
        }

        cacheManager.getCache("fareQuote").put(traceId, new FareQuoteCacheEntry(outboundFlightDetails, inboundFlightDetails));

//        List<FlightFareQuoteResponse> result = new ArrayList<>();
        List<Map<String, FlightFareQuoteResponse>> result = new ArrayList<>();

        Map<String, FlightFareQuoteResponse> outboundResult = Map.of("outboundFareQuote", outboundFareQuote);
        result.add(outboundResult);
//        result.add(outboundFareQuote);
        if(inboundFareQuote != null) {
            Map<String, FlightFareQuoteResponse> inboundResult = Map.of("inboundFareQuote", inboundFareQuote);
            result.add(inboundResult);
        }

        return result;
    }

    public FlightBookingTicketingCombinedResponse flightBookAndTicket(FlightBookingTicketingRequest request) throws Exception {
        FlightBookingTicketingCombinedResponse combinedResponse = new FlightBookingTicketingCombinedResponse();

        Cache cache = cacheManager.getCache("fareQuote");
        Cache.ValueWrapper wrapper = cache.get(request.getTraceId());

        FlightFareQuoteDetailsResponse obFlight = null;
        FlightFareQuoteDetailsResponse ibFlight = null;

        Map<String, Object> obReq = TboFlightRequestMapper.mapToBookingTicketingRequest(request, wrapper);
        Map<String, Object> ibReq = TboFlightRequestMapper.mapToBookingTicketingRequest(request, wrapper);

        List<FlightBookingResponseNonLcc> bookings = new ArrayList<>();
        FlightBookingResponseNonLcc obBooking = null;
        FlightBookingResponseNonLcc ibBooking = null;

        List<FlightTicketResponse> tickets = new ArrayList<>();
        FlightTicketResponse obTicket = null;
        FlightTicketResponse ibTicket = null;

        if(wrapper != null) {
            FareQuoteCacheEntry entry = (FareQuoteCacheEntry) wrapper.get();
            obFlight = (FlightFareQuoteDetailsResponse) (entry != null ? entry.getOutboundFlight() : null);
            ibFlight = (FlightFareQuoteDetailsResponse) (entry != null ? entry.getInboundFlight() : null);
        }

        if(obFlight.isLCC()) {
            obTicket = getFlightTicket(obReq.get("outbound"));

            tickets.add(obTicket);
        }

        if (!obFlight.isLCC()) {
            obBooking = getFlightBooking(obReq.get("outbound"));

            bookings.add(obBooking);

//            saveBookingToDb(obBooking);
//            Map<String, Object> reqBody = new HashMap<>();
//            reqBody.put("EndUserIp", "192.168.97.1");
//            reqBody.put("TokenId", TboAuthService.getToken());
//            reqBody.put("TraceId", obBooking.getTraceId());
//            reqBody.put("PNR", obBooking.getBookingDetails().getPnr());
//            reqBody.put("BookingId", obBooking.getBookingDetails().getBookingId());
//
//            obTicket = getFlightTicket(reqBody);
        }

        if(ibFlight != null) {
            if(ibFlight.isLCC()) {
                ibTicket = getFlightTicket(ibReq.get("inbound"));

                tickets.add(ibTicket);
            }

            if(!ibFlight.isLCC()) {
                ibBooking = getFlightBooking(ibReq);

                bookings.add(ibBooking);

//                saveBookingToDb(ibBooking);
//                Map<String, Object> reqBody = new HashMap<>();
//                reqBody.put("EndUserIp", "192.168.97.1");
//                reqBody.put("TokenId", TboAuthService.getToken());
//                reqBody.put("TraceId", ibBooking.getTraceId());
//                reqBody.put("PNR", ibBooking.getBookingDetails().getPnr());
//                reqBody.put("BookingId", ibBooking.getBookingDetails().getBookingId());
//
//                ibTicket = getFlightTicket(reqBody);
            }
        }

//        List<FlightTicketResponse> result = new ArrayList<>();
//        result.add(obTicket);
//
//        if(ibTicket != null) result.add(ibTicket);
        combinedResponse.setBookingResponses(bookings);
        combinedResponse.setTicketResponses(tickets);

        return combinedResponse;
    }

    public FlightTicketResponse getFlightTicket(Object requestBody) throws Exception {
        ResponseEntity<TboApiFlightTicketResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Ticket",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightTicketResponseDto>() {}
        );


        if(!Objects.equals(response.getBody().getResponse().getError().getErrorMessage(), "")) {
            throw new Exception(response.getBody().getResponse().getError().getErrorMessage());
        }
        return tboFlightTicketMapper.toFlightTicketResponse(response.getBody());
    }

    public FlightBookingResponseNonLcc getFlightBooking(Object requestBody) throws Exception {
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

        return tboFlightBookingResponseMapper.mapToBookingResponse(response.getBody());
    }

    public void saveBookingToDb(FlightBookingResponseNonLcc booking) {
        FlightBooking flightBooking = new FlightBooking();
        flightBooking.setBookingId(booking.getBookingDetails().getBookingId());
        flightBooking.setBookingStatus(BookingStatusDb.PENDING);
        flightBooking.setPnr(booking.getBookingDetails().getPnr());
        flightBooking.setLcc(booking.getBookingDetails().getFlightDetails().isLCC());
        flightBooking.setDomestic(booking.getBookingDetails().getFlightDetails().isDomestic());

        flightBookingRepository.save(flightBooking);
    }


    public TBOGetBookingDetailsResponse getBookingDetails(TBOGetBookingDetailsRequest request){
        log.info("Fetching booking details for PNR: {} and BookingId: {}", request.getPnr(), request.getBookingId());
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToBookingDetailsRequest(request);

        ResponseEntity<TBOGetBookingDetailsResponse> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/GetBookingDetails",
                HttpMethod.POST,
                new HashMap<>(),                // headers
                requestBody,                        // body
                new ParameterizedTypeReference<TBOGetBookingDetailsResponse>() {} // expected response type
        );

        return response.getBody();
    }

}

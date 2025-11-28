package com.jamuara.crs.flight.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamuara.crs.admin.priceChanges.DynamicPricingService;
import com.jamuara.crs.admin.priceChanges.PriceRule;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.service.EmailService;
import com.jamuara.crs.common.service.PdfService;
import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.common.service.RestService;
import com.jamuara.crs.enums.TicketStatus;
import com.jamuara.crs.flight.dto.tbo.*;
import com.jamuara.crs.flight.dto.tbo.book.*;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchMulticityRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchResponse;
import com.jamuara.crs.flight.dto.tbo.search.TboApiFlightResponseDto;
import com.jamuara.crs.flight.mapper.*;
import com.jamuara.crs.model.Reservation;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@EnableCaching
@Slf4j
public class TboFlightService {
    RestService restService;

    TboFlightSearchResponseMapper tboFlightSearchResponseMapper;

    TboFlightBookingResponseMapper tboFlightBookingResponseMapper;

    TboFlightFareRulesMapper tboFlightFareRulesMapper;

    TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper;

    TboFlightTicketMapper tboFlightTicketMapper;

    TboFetchFlightBookingResponseMapper tboFetchFlightBookingResponseMapper;

    @Autowired(required = false)
    ReservationService reservationService;

    CacheManager cacheManager;

    private final PdfService pdfService;

    private final EmailService emailService;

    @Autowired
    private Helper helper;

    private final String TBO_FLIGHT_URL = "http://api.tektravels.com/BookingEngineService_Air/AirService.svc/rest";

    @Autowired
    private DynamicPricingService dynamicPricingService;


    public TboFlightService(
            RestService restService,
            TboFlightSearchResponseMapper tboFlightSearchResponseMapper,
            TboFlightBookingResponseMapper tboFlightBookingResponseMapper,
            TboFlightFareRulesMapper tboFlightFareRulesMapper,
            TboFlightFareQuoteResponseMapper tboFlightFareQuoteResponseMapper,
            TboFlightTicketMapper tboFlightTicketMapper,
            TboFetchFlightBookingResponseMapper tboFetchFlightBookingResponseMapper,
            CacheManager cacheManager,
            PdfService pdfService,
            EmailService emailService
    ) {
        this.restService = restService;
        this.tboFlightSearchResponseMapper = tboFlightSearchResponseMapper;
        this.tboFlightBookingResponseMapper = tboFlightBookingResponseMapper;
        this.tboFlightFareRulesMapper = tboFlightFareRulesMapper;
        this.tboFlightFareQuoteResponseMapper = tboFlightFareQuoteResponseMapper;
        this.tboFlightTicketMapper = tboFlightTicketMapper;
        this.tboFetchFlightBookingResponseMapper = tboFetchFlightBookingResponseMapper;
        this.cacheManager = cacheManager;
        this.pdfService = pdfService;
        this.emailService = emailService;
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


        FlightSearchResponse responseNew=tboFlightSearchResponseMapper.mapToFlightSearchResponse(response.getBody().getResponse());
        dynamicPricingService.applyMarkupOnSearch(responseNew, searchRequest);

/*
        //  STORE TRIP TYPE USING TRACE ID
        PriceRule.TripType tripType =
                dynamicPricingService.getTripType(
                        searchRequest.getOriginLocationCode(),
                        searchRequest.getDestinationLocationCode()
                );
*/

        return responseNew;
    }

    public FlightSearchResponse flightMulticitySearch(FlightSearchMulticityRequest searchMulticityRequest) throws Exception {
        Map<String, Object> requestBody = TboFlightRequestMapper.mapDtoToMulticityRequest(searchMulticityRequest);

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


        //  MAP RESPONSE
        FlightSearchResponse responseNew =
                tboFlightSearchResponseMapper.mapToFlightSearchResponse(
                        response.getBody().getResponse()
                );


        //  APPLY MARKUP FOR MULTICITY
        dynamicPricingService.applyMarkupOnMulticitySearch(
                responseNew, searchMulticityRequest
        );


        return responseNew;
    }

    public List<Map<String, FlightFareRuleResponse>> flightFareRules(FlightFareRulesCumQuoteRequest request) throws Exception {

        log.info("flight fare rules request received");
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFareQuoteRulesRequest(request);

        ResponseEntity<TboApiFareRuleResponseDto> outboundResponse = restService.sendRequest(
                TBO_FLIGHT_URL + "/FareRule",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody.get("outbound"),
                new ParameterizedTypeReference<TboApiFareRuleResponseDto>() {}
        );

        System.out.println(outboundResponse.getBody().toString());
        if(!Objects.equals(outboundResponse.getBody().getResponse().getError().getErrorMessage(), "")) {
            throw new Exception(outboundResponse.getBody().getResponse().getError().getErrorMessage());
        }

        FlightFareRuleResponse outboundFareRules = tboFlightFareRulesMapper.mapToFareRulesResponse(outboundResponse.getBody().getResponse());

        ResponseEntity<TboApiFareRuleResponseDto> inboundResponse = null;
        if(requestBody.containsKey("inbound")) {
            inboundResponse = restService.sendRequest(
                    TBO_FLIGHT_URL + "/FareRule",
                    HttpMethod.POST,
                    new HashMap<>(),
                    requestBody.get("inbound"),
                    new ParameterizedTypeReference<TboApiFareRuleResponseDto>() {}
            );

            if(!Objects.equals(inboundResponse.getBody().getResponse().getError().getErrorMessage(), "")) {
                throw new Exception(outboundResponse.getBody().getResponse().getError().getErrorMessage());
            }
        }

        Object inboundFlightDetails = null;
        FlightFareRuleResponse inboundFareRules = new FlightFareRuleResponse();

        if(inboundResponse != null) inboundFareRules = tboFlightFareRulesMapper.mapToFareRulesResponse(inboundResponse.getBody().getResponse());

        List<Map<String, FlightFareRuleResponse>> result = new ArrayList<>();

        Map<String, FlightFareRuleResponse> outboundResult = Map.of("outboundFareQuote", outboundFareRules);
        result.add(outboundResult);

        if(inboundFareRules != null) {
            Map<String, FlightFareRuleResponse> inboundResult = Map.of("inboundFareQuote", inboundFareRules);
            result.add(inboundResult);
        }

        return result;
    }

    public List<Map<String, FlightFareQuoteResponse>> flightFareQuote(FlightFareRulesCumQuoteRequest request) throws Exception {

        log.info("flight fare quote request received");
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFareQuoteRulesRequest(request);

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

        //  Apply markup using outbound resultIndex
        dynamicPricingService.applyMarkupByResultIndex(
                outboundFareQuote,
                request.getResultIndexOutbound()
        );

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

            //  Apply markup using inbound resultIndex
            dynamicPricingService.applyMarkupByResultIndex(
                    inboundFareQuote,
                    request.getResultIndexInbound()
            );

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

    public List<FetchFlightBookingResponse> flightBookAndTicket(FlightBookingTicketingRequest request) throws Exception {
        FlightBookingTicketingCombinedResponse combinedResponse = new FlightBookingTicketingCombinedResponse();

        Cache cache = cacheManager.getCache("fareQuote");
        Cache.ValueWrapper wrapper = cache.get(request.getTraceId());

        FlightFareQuoteDetailsResponse obFlight = null;
        FlightFareQuoteDetailsResponse ibFlight = null;

        Map<String, Object> combinedReq = TboFlightRequestMapper.mapToBookingTicketingRequest(request, wrapper);
//        Map<String, Object> combinedReq = TboFlightRequestMapper.mapToBookingTicketingRequest(request, wrapper);

        List<FetchFlightBookingResponse> bookings = new ArrayList<>();
//        FlightBookingResponseNonLcc obBooking = null;
//        FlightBookingResponseNonLcc ibBooking = null;

        FetchFlightBookingResponse obBooking = null;
        FetchFlightBookingResponse ibBooking = null;

//        List<FlightTicketResponse> tickets = new ArrayList<>();

//        FetchFlightBookingResponse obTicket = null;
//        FetchFlightBookingResponse ibTicket = null;

        if(wrapper != null) {
            FareQuoteCacheEntry entry = (FareQuoteCacheEntry) wrapper.get();
            obFlight = (FlightFareQuoteDetailsResponse) (entry != null ? entry.getOutboundFlight() : null);
            ibFlight = (FlightFareQuoteDetailsResponse) (entry != null ? entry.getInboundFlight() : null);
        }

        if(obFlight != null) {
            if(obFlight.isLCC()) {
                obBooking = getFlightTicket((Map<String, Object>) combinedReq.get("outbound"));

                bookings.add(obBooking);
            }

            if (!obFlight.isLCC()) {
                obBooking = getFlightBooking((Map<String, Object>) combinedReq.get("outbound"));

                bookings.add(obBooking);
            }
        }

        if(ibFlight != null) {
            if(ibFlight.isLCC()) {
                ibBooking = getFlightTicket((Map<String, Object>) combinedReq.get("inbound"));

                bookings.add(ibBooking);
            }

            if(!ibFlight.isLCC()) {
                ibBooking = getFlightBooking((Map<String, Object>) combinedReq.get("inbound"));

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

        emitFlightBookingEvent(bookings);
        return bookings;
    }

    public FetchFlightBookingResponse getFlightTicket(Map<String, Object> requestBody) throws Exception {
        ResponseEntity<TboApiFlightTicketResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Ticket",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightTicketResponseDto>() {}
        );

        TboApiFlightTicketResponseDto body = response.getBody();
        TboApiFlightTicketResponseDto.BookingResponseWrapper wrapper = body.getResponse();
//        var errorMessage = wrapper != null && wrapper.getError() != null ? wrapper.getError() : "No error field present";
        System.out.println("ticket response: " + response.getBody().toString());
//        String errorMessage =
                Optional.ofNullable(wrapper)
                        .map(TboApiFlightTicketResponseDto.BookingResponseWrapper::getError)
                                .map(TboApiFlightTicketResponseDto.ErrorResponse::getErrorMessage)
                                        .filter(msg -> !msg.isBlank())
                                                .ifPresent((msg) -> {
                                                    throw new RuntimeException(msg);
                                                });
//                                        .orElse("invalid error from tbo");

        log.info("ticketing response: {}", wrapper != null && wrapper.getError() != null ? wrapper.getError() : "No error field present");

//        if(!errorMessage.isBlank()) {
//            throw new Exception(errorMessage);
//        }

        TboApiFlightTicketResponseDto.BookingResponseDetails responseDetails = response.getBody().getResponse().getResponse();
//        FlightTicketResponse ticket = tboFlightTicketMapper.toFlightTicketResponse(response.getBody());

        FetchBookingRequest fetchBookingRequest = new FetchBookingRequest();

        FetchFlightBookingResponse fetchBooking = new FetchFlightBookingResponse();

        if(responseDetails.getPnr() == null || responseDetails.getPnr().isEmpty()) {
            fetchBookingRequest.setBookingId((String) requestBody.get("BookingId"));
            fetchBookingRequest.setPnr((String) requestBody.get("PNR"));
//            fetchBookingRequest.setTraceId((String) requestBody.get("TraceId"));
            fetchBooking = fetchBookingDetailsAfterBookTicket(fetchBookingRequest);
            return fetchBooking;
        }

        fetchBookingRequest.setPnr(responseDetails.getPnr());
        fetchBookingRequest.setFirstName(
                responseDetails.getFlightItinerary()
                .getPassenger().stream().filter(TboApiFlightTicketResponseDto.Passenger::isLeadPax).toList()
                .get(0).getFirstName()
        );

        fetchBooking = fetchBookingDetailsAfterBookTicket(fetchBookingRequest);

        return fetchBooking;
    }

    public FetchFlightBookingResponse getFlightBooking(Map<String, Object> requestBody) throws Exception {
        ResponseEntity<TboApiFlightBookingResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Book",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightBookingResponseDto>() {}
        );
        TboApiFlightBookingResponseDto body = response.getBody();
//        var errorMessage = body != null && body.getError() != null ? body.getError() : "No error field present";

//        String errorMessage =
            Optional.ofNullable(body)
                .map(TboApiFlightBookingResponseDto::getResponse)
                        .map(TboApiFlightBookingResponseDto.Response::getError)
                                .map(TboApiFlightBookingResponseDto.Response.ErrorDetail::getErrorMessage)
                                        .filter(msg -> !msg.isBlank())
                                                .ifPresent(msg -> {
                                                    throw new RuntimeException(msg);
                                                });
//                                        .orElseThrow();

        log.info("response body: " + response.getBody().toString());
        log.info("booking response: {}", body != null && body.getResponse().getError() != null ? body.getResponse().getError() : response.getBody());
//        System.out.println("error message: " + errorMessage.get());
//
//        if(errorMessage.isPresent()) {
//            throw new Exception(errorMessage.get());
//        }

//        FlightBookingResponseNonLcc flightBookingResponseNonLcc = tboFlightBookingResponseMapper.mapToBookingResponse(response.getBody());

        TboApiFlightBookingResponseDto.Response.ResponseData responseDetails = response.getBody().getResponse().getResponse();

        String lastTicketDate = "";

        if(responseDetails != null) lastTicketDate = responseDetails.getFlightItinerary().getLastTicketDate();

        FetchBookingRequest fetchBookingRequest = new FetchBookingRequest();

        FetchFlightBookingResponse fetchBooking = new FetchFlightBookingResponse();

        if(responseDetails.getPnr() == null || responseDetails.getPnr().isEmpty()) {
            ResponseEntity<TboApiFlightBookingResponseDto> secondBookingCall = restService.sendRequest(
                    TBO_FLIGHT_URL + "/Book",
                    HttpMethod.POST,
                    new HashMap<>(),
                    requestBody,
                    new ParameterizedTypeReference<TboApiFlightBookingResponseDto>() {}
            );
            System.out.println("second book call: " + secondBookingCall.toString());

            String errorString = secondBookingCall.getBody().getResponse().getError().getErrorMessage();
            String regex = "PNR\\s([A-Za-z0-9]{6})";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(errorString);

            if(matcher.find()) {
                System.out.println("matcher: " + matcher.toString());
                String pnr = matcher.group(1);
//                fetchBookingRequest.setTraceId((String) requestBody.get("TraceId"));
                List<Map<String, Object>> passengers = (List<Map<String, Object>>) requestBody.get("Passengers");
                Map<String, Object> firstPass = passengers.get(0);
                String firstName = (String) firstPass.get("FirstName");

                fetchBookingRequest.setPnr(pnr);
                fetchBookingRequest.setFirstName(firstName);
//                System.out.println("pnr null case req body: " + fetchBookingRequest.toString());
                fetchBooking = fetchBookingDetailsAfterBookTicket(fetchBookingRequest);
                return fetchBooking;
            }

//            fetchBookingRequest.setTraceId((String) requestBody.get("TraceId"));

        }

        fetchBookingRequest.setPnr(responseDetails.getPnr());

        fetchBookingRequest.setFirstName(
                responseDetails.getFlightItinerary()
                .getPassengers().stream().filter(TboApiFlightBookingResponseDto.Response.Passenger::isLeadPax).toList()
                .get(0).getFirstName()
        );
//        System.out.println("pnr not null case req body: " + fetchBookingRequest.toString());
        fetchBooking = fetchBookingDetailsAfterBookTicket(fetchBookingRequest);

        return fetchBooking;
    }

    public void saveBookingToDb(FetchFlightBookingResponse booking, TboApiFetchFlightBookingResponseDto rawResponse) throws JsonProcessingException {
        log.info("saving booking data to database");
        Reservation.BookingStatus status = null;
        status = booking.getTicketBookingDetails().getFlightDetails().isLCC() ? Reservation.BookingStatus.CONFIRM : Reservation.BookingStatus.PENDING;

        Reservation res = reservationService.createReservationTbo(booking,  status, rawResponse);
        log.info("reservation saved successfully to the database with booking id: {}", res.getBookingId());
    }

    public FetchFlightBookingResponse fetchBookingDetailsAfterBookTicket(FetchBookingRequest request) throws Exception {
        log.info("Fetching booking details for PNR: {} and BookingId: {}", request.getPnr(), request.getBookingId());
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFetchBookingDetailsRequest(request);

        ResponseEntity<TboApiFetchFlightBookingResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/GetBookingDetails",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFetchFlightBookingResponseDto>() {}
        );

        TboApiFetchFlightBookingResponseDto body = response.getBody();
        TboApiFetchFlightBookingResponseDto.Response wrapper = body.getResponse();
//        var errorMessage = wrapper != null && wrapper.getError() != null ? wrapper.getError() : "No error field present";

//        String errorMessage =
                Optional.ofNullable(wrapper)
                    .map(TboApiFetchFlightBookingResponseDto.Response::getError)
                        .map(TboApiFetchFlightBookingResponseDto.Response.Error::getErrorMessage)
                            .filter(msg -> !msg.isBlank())
                                .ifPresent(msg -> {
                                    throw new RuntimeException(msg);
                                });
//                .orElse("invalid error from tbo");

        log.info("fetch booking response: {}", wrapper != null && wrapper.getError() != null ? wrapper.getError() : response.getBody().toString());

//        if(!errorMessage.isBlank()) {
//            throw new Exception(errorMessage);
//        }

        FetchFlightBookingResponse fetchedFlight = tboFetchFlightBookingResponseMapper.toFetchFlightBookingResponse(response.getBody());
        String arrTime = fetchedFlight.getTicketBookingDetails().getFlightDetails().getFlightLegs().get(0).getArrivalDateTime();
        String depTime = fetchedFlight.getTicketBookingDetails().getFlightDetails().getFlightLegs().get(0).getDepartureDateTime();
//        return tboFlightTicketMapper.toFlightTicketResponse(response.getBody());

        //  APPLY MARKUP HERE
        dynamicPricingService.applyMarkupOnFetchBooking(fetchedFlight);
        saveBookingToDb(fetchedFlight, response.getBody());

        return fetchedFlight;
    }

    public FetchFlightBookingResponse fetchBookingDetails(FetchBookingRequest request) throws Exception {
        log.info("Fetching booking details for PNR: {} and BookingId: {}", request.getPnr(), request.getBookingId());
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToFetchBookingDetailsRequest(request);

        ResponseEntity<TboApiFetchFlightBookingResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/GetBookingDetails",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFetchFlightBookingResponseDto>() {}
        );

        TboApiFetchFlightBookingResponseDto body = response.getBody();
        TboApiFetchFlightBookingResponseDto.Response wrapper = body.getResponse();
//        var errorMessage = wrapper != null && wrapper.getError() != null ? wrapper.getError() : "No error field present";

                Optional.ofNullable(wrapper)
                .map(TboApiFetchFlightBookingResponseDto.Response::getError)
                .map(TboApiFetchFlightBookingResponseDto.Response.Error::getErrorMessage)
                .filter(msg -> !msg.isBlank())
                .ifPresent((msg) -> {
                    throw new RuntimeException(msg);
                });

        log.info("fetch booking response: {}", wrapper != null && wrapper.getError() != null ? wrapper.getError() : response.getBody().toString());

       // return tboFetchFlightBookingResponseMapper.toFetchFlightBookingResponse(response.getBody());

        //  1. MAP RESPONSE
        FetchFlightBookingResponse fetchedFlight =
                tboFetchFlightBookingResponseMapper
                        .toFetchFlightBookingResponse(response.getBody());

        //  2. APPLY MARKUP ON FINAL FARE (SAFE & CONDITIONAL)
        dynamicPricingService.applyMarkupOnFetchBooking(fetchedFlight);

        //  3. RETURN UPDATED RESPONSE
        return fetchedFlight;
    }

   /* public  List<Reservation> getAllBookings(Reservation.BookingStatus status) {
        return reservationService.findReservationsByStatus(status);
    }

    public  List<Reservation> getAllBookings() {
        return reservationService.findAllReservations();
    }*/

    public FlightTicketResponse getFlightTicketNonLcc(FlightTicketRequestNonLcc requestNonLcc) {
        Map<String, Object> requestBody = TboFlightRequestMapper.mapToTicketingRequestNonLcc(requestNonLcc);

        ResponseEntity<TboApiFlightTicketResponseDto> response = restService.sendRequest(
                TBO_FLIGHT_URL + "/Ticket",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<TboApiFlightTicketResponseDto>() {}
        );

        TboApiFlightTicketResponseDto ticketResponseDto = response.getBody();
        TboApiFlightTicketResponseDto.BookingResponseWrapper wrapper = ticketResponseDto.getResponse();

        Optional.ofNullable(wrapper)
                .map(TboApiFlightTicketResponseDto.BookingResponseWrapper::getError)
                .map(TboApiFlightTicketResponseDto.ErrorResponse::getErrorMessage)
                .filter(msg -> !msg.isBlank())
                .ifPresent((msg) -> {
                    throw new RuntimeException(msg);
                });

         return tboFlightTicketMapper.toFlightTicketResponse(response.getBody());
    }

    public void updateBookingStatus(String bookingId, Reservation.BookingStatus status) throws Exception {
        log.info("updating flight status for booking id {} to status {}", bookingId, status );

        Reservation reservation = reservationService.findByBookingId(bookingId);

//        Map<String, Object> reqBody = new HashMap<>();
//        reqBody.put("EndUserIp", "192.168.97.10");
//        reqBody.put("TokenId", TboAuthService.getToken());
//        reqBody.put("PNR",  reservation.getPnr());
//        reqBody.put("BookingId", reservation.getBookingId());

        FlightTicketRequestNonLcc reqBody = new FlightTicketRequestNonLcc();
        reqBody.setBookingId(reservation.getBookingId());
        reqBody.setPnr(reservation.getPnr());

        FlightTicketResponse flight = getFlightTicketNonLcc(reqBody);
        log.info("flight ticket while updating status: {}", flight.toString());
        if(flight.getTicketBookingDetails().getTicketStatus() == TicketStatus.Successful) {
            reservation.setBookingStatus(status);
            reservationService.saveReservation(reservation);
        }

        log.info("reservation status updated successfully");
    }

    public void emitFlightBookingEvent(List<FetchFlightBookingResponse> bookings) throws MessagingException, IOException {
        log.info("emitting flight booking event");
        Map<String, byte[]> ticketPdfs = new HashMap<>();
        List<Reservation> reservations = bookings.stream().map(b ->
                reservationService.findByBookingIdWithAllRelations(b.getTicketBookingDetails().getBookingId())
        ).toList();

        List<String> htmlTickets = reservations.stream().map(b -> {
            Context ctx = new Context();
            ctx.setVariable("booking", b);
            return helper.getHtmlBody("ticket-pdf", ctx);
        }).toList();

        for(int i = 0; i < htmlTickets.size(); i++) {
            String html = htmlTickets.get(i);
            if(htmlTickets.size() == 1) {
                byte[] pdf = pdfService.generatePdf(html);
                ticketPdfs.put("ticket", pdf);
                pdfService. savePdf("ticket-" + reservations.get(i).getBookingId(), pdf);
            } else if(htmlTickets.size() == 2) {
                byte[] pdf = pdfService.generatePdf(html);
                ticketPdfs.put(i == 0 ? "outbound" : "inbound", pdf);
                pdfService. savePdf("ticket-" + reservations.get(i).getBookingId(), pdf);
            } else {
                byte[] pdf = pdfService.generatePdf(html);
                ticketPdfs.put("trip "+i, pdf);
                pdfService. savePdf("ticket-" + reservations.get(i).getBookingId(), pdf);
            }
        }

        Context context = new Context();
        context.setVariable("bookings", reservations);

        String body = helper.getHtmlBody("ticket-booking", context);
        String receiverEmail = bookings.get(0).getTicketBookingDetails().getFlightDetails().getTravelers().get(0).getEmail();
        //emailService.sendEmail("rthakur.0211@gmail.com", "Ticket confirmation", body, ticketPdfs);
    }
}

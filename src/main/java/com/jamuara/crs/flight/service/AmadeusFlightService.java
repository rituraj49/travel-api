package com.jamuara.crs.flight.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.amadeus.resources.FlightPrice;
import com.amadeus.resources.Resource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.common.service.UserLogsService;
import com.jamuara.crs.flight.dto.*;
import com.jamuara.crs.flight.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@Primary
@Slf4j
@Profile("!offline")
public class AmadeusFlightService implements IFlightService {
    Amadeus amadeusClient;

    AmadeusFlightOfferMapper flightOfferMapper;

    AmadeusFlightPricingMapper flightPricingMapper;

    AmadeusFlightTravelerRequestMapper travelerRequestMapper;

    AmadeusFlightBookingResponseMapper flightBookingResponseMapper;

    @Autowired(required = false)
    private ReservationService reservationService;

    @Autowired(required = false)
    private UserLogsService userLogsService;

    public AmadeusFlightService(
            Amadeus amadeusClient,
            AmadeusFlightOfferMapper flightOfferMapper,
            AmadeusFlightPricingMapper flightPricingMapper,
            AmadeusFlightTravelerRequestMapper travelerRequestMapper,
            AmadeusFlightBookingResponseMapper flightBookingResponseMapper
    ) {
        this.amadeusClient = amadeusClient;
        this.flightOfferMapper = flightOfferMapper;
        this.flightPricingMapper = flightPricingMapper;
        this.travelerRequestMapper = travelerRequestMapper;
        this.flightBookingResponseMapper = flightBookingResponseMapper;
    }

//    @Cacheable("flightOffers")
    public List<FlightAvailabilityResponse> searchFlights(FlightSearchRequest request) throws ResponseException {
        log.info("cache missed, sending flight search request to amadeus");
        log.info("flight offers request parameters: {}", request.toString());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> paramsMap = mapper.convertValue(request, new TypeReference<Map<String, String>>() {});

        Params params = null;
        for(Map.Entry<String, String> entry: paramsMap.entrySet()) {
            if(params == null) {
                params = Params.with(entry.getKey(), entry.getValue());
            } else {
                params.and(entry.getKey(), entry.getValue());
            }
        }

        log.info("params for amadeus api: {}", params);
        Response rawResponse = amadeusClient.get("/v2/shopping/flight-offers", params);
//        FlightOfferSearch[] offers = amadeusClient.shopping.flightOffersSearch.get(params);
//        System.out.println("offers sdkmethod: " + Arrays.toString(offers));
//        System.out.println("raw res: " + rawResponse.toString());
        JsonObject jsonRes = rawResponse.getResult().getAsJsonObject();
        JsonObject dictionaries = jsonRes.getAsJsonObject("dictionaries");

        FlightOfferSearch[] flightOffers = (FlightOfferSearch[]) Resource.fromArray(rawResponse, FlightOfferSearch[].class);
        log.info("{} flight offers found", flightOffers.length);

        List<FlightOfferSearch> flightOfferList = Arrays.asList(flightOffers);

        List<FlightAvailabilityResponse> responseList = flightOfferList.stream()
                .map(f -> flightOfferMapper.toFlightAvailabilityResponse(f, dictionaries))
                .toList();
        return responseList;
    }

    @Cacheable("flightOffers")
    public List<FlightAvailabilityResponse> searchMultiCityFlights
            (FlightAvailabilityRequest flightOfferSearchRequestDto) throws ResponseException, JsonProcessingException {
        log.info("cache missed, sending multi-city flight search request to amadeus");
        Map<String, Object> dtoMap = AmadeusFlightSearchRequestMapper.mapDtoToFlightSearchRequest(flightOfferSearchRequestDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(dtoMap);
        log.info("Flight search request body sent to amadeus: {}", body);

        Response rawResponse = amadeusClient.post("/v2/shopping/flight-offers", body);

        FlightOfferSearch[] offers = (FlightOfferSearch[]) Resource.fromArray(rawResponse, FlightOfferSearch[].class);
        JsonObject json = rawResponse.getResult().getAsJsonObject();
        JsonObject dictionaries = json.getAsJsonObject("dictionaries");

        List<FlightOfferSearch> flightOfferList = Arrays.asList(offers);
        List<FlightAvailabilityResponse> responseList = flightOfferList.stream()
                .map(f -> flightOfferMapper.toFlightAvailabilityResponse(f, dictionaries))
                .toList();
        log.info("{} flight offers found", responseList.size());
        return responseList;
    }

    public FlightPricingResponse confirmFlightPrice(String flightOfferString) throws ResponseException, JsonProcessingException {

        String wrappedString = AmadeusFlightPricingRequestMapper.mapToPricingRequestString(flightOfferString);
            Response rawResponse = amadeusClient.post(
                    "/v1/shopping/flight-offers/pricing",
                    Params.with("include", "credit-card-fees,bags,other-services,detailed-fare-rules")
                            .and("forceClass", "false"),
                    wrappedString
            );
            FlightPrice flightPrice = (FlightPrice) Resource.fromObject(rawResponse, FlightPrice.class);

            JsonObject jsonRes = rawResponse.getResult().getAsJsonObject();
            JsonObject creditCardFees = jsonRes.getAsJsonObject("included").getAsJsonObject("credit-card-fees");
            FlightPricingResponse response = flightPricingMapper.toFlightPricingResponse(flightPrice.getFlightOffers()[0], creditCardFees);

            return response;
    }

    public FlightBookingResponse createFlightOrder(FlightBookingRequest flightOrderRequest) throws ResponseException, JsonProcessingException {
        log.info("Creating flight order with received request");
        Gson gson = new Gson();

        FlightOfferSearch offer = gson.fromJson(flightOrderRequest.getFlightOffer(), FlightOfferSearch.class);

        List<FlightOrder.Traveler> travelerList = travelerRequestMapper.toTravelerList(flightOrderRequest.getTravelers());

        FlightOrder.Traveler[] flightTravelers = travelerList.toArray(new FlightOrder.Traveler[0]);

        FlightOrder order = amadeusClient.booking.flightOrders.post(offer, flightTravelers);

        FlightBookingResponse bookingResponse = flightBookingResponseMapper.toFlightBookingResponse(order);
        bookingResponse.getFlightOffer().setPricingAdditionalInfo(null);
//        emitFlightBookingEvent(bookingResponse);

        reservationService.createReservation(bookingResponse);
        userLogsService.createUserLogs(flightOrderRequest,bookingResponse);
        log.info("flight booking confirmed with id: {}", bookingResponse.getOrderId());
        return bookingResponse;
    }

    public FlightBookingResponse fetchFlightOrder(String orderId) throws ResponseException {
        log.info("Fetching flight order with id: {}", orderId);

        FlightOrder  order = amadeusClient.booking.flightOrder(orderId).get();

        FlightBookingResponse bookingResponse = flightBookingResponseMapper.toFlightBookingResponse(order);
        bookingResponse.getFlightOffer().setPricingAdditionalInfo(null);
        log.info("flight booking fetched with id: {}", bookingResponse.getOrderId());
        return bookingResponse;
    }
}

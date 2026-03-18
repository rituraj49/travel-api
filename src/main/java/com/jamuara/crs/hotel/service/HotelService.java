package com.jamuara.crs.hotel.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.HotelOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.repository.HotelReservationRepository;
import com.jamuara.crs.currency.dto.Money;
import com.jamuara.crs.currency.service.CurrencyService;
import com.jamuara.crs.enums.Amenity;
import com.jamuara.crs.hotel.dto.HotelBookingRequestDto;
import com.jamuara.crs.hotel.dto.HotelSearchRequestDto;
import com.jamuara.crs.hotel.mappers.HotelReservationMapper;
import com.jamuara.crs.hotel.mappers.HotelSearchResponseMapper;
import com.jamuara.crs.hotel.model.HotelOfferResponse;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import com.jamuara.crs.model.HotelReservation;
import com.jamuara.crs.model.UserProfile;
import com.jamuara.crs.profile.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HotelService implements IHotelService {
    private final Amadeus amadeusClient;

    private HotelSearchResponseMapper searchResponseMapper;

    private HotelReservationMapper hotelReservationMapper;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private HotelReservationRepository hotelReservationRepository;

    @Autowired
    private Gson gson;

    @Autowired
    CurrencyService currencyService;

    public HotelService(Amadeus amadeusClient, HotelSearchResponseMapper searchResponseMapper, HotelReservationMapper hotelReservationMapper) {
        this.amadeusClient = amadeusClient;
        this.searchResponseMapper = searchResponseMapper;
        this.hotelReservationMapper = hotelReservationMapper;
    }

    @Override
    public List<HotelSearchResponse> getHotels(
            String cityCode, Integer radius, String radiusUnit, List<String> amenities, List<String> ratings
    ) throws ResponseException {

            Params params = Params.with("cityCode", cityCode);

            if (radius != null) params.and("radius", radius);
            if (radiusUnit != null) params.and("radiusUnit", radiusUnit);
            if (amenities != null && !amenities.isEmpty()) params.and("amenities", String.join(",", amenities));
            if (ratings != null && !ratings.isEmpty()) params.and("ratings", String.join(",", ratings));

            Hotel[] hotels = amadeusClient.referenceData.locations.hotels.byCity.get(params);
        System.out.println(Arrays.toString(hotels));
            return Arrays.stream(hotels)
                    .map(searchResponseMapper::toHotelSearchResponse)
                    .collect(Collectors.toList());
        }

    @Override
    public List<HotelOfferResponse> getOffers(Map<String, String> paramsMap) throws Exception {
        Params params = null;
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (params == null) {
                params = Params.with(entry.getKey(), entry.getValue());
            } else {
                params.and(entry.getKey(), entry.getValue());
            }
        }

        HotelOfferSearch[] offers = amadeusClient.shopping.hotelOffersSearch.get(params);

        String json = gson.toJson(offers);
        HotelOfferResponse[] hotelOffers = gson.fromJson(json, HotelOfferResponse[].class);

        return Arrays.asList(hotelOffers);
    }

    public List<HotelOfferResponse> getHotelOffers(HotelSearchRequestDto requestDto) throws ResponseException {
        log.info("params received for hotel offer search: " + requestDto.toString());
        Params params = Params.with("cityCode", requestDto.getCityCode());
        params.and("radius", requestDto.getRadius() != 0 ? requestDto.getRadius() : 5);
        params.and("radiusUnit", requestDto.getRadiusUnit() != null ? requestDto.getRadiusUnit() : "KM");
        if(requestDto.getAmenities() != null && !requestDto.getAmenities().isEmpty()) {
            List<String> amenities = requestDto.getAmenities().stream().map(Amenity::fromKey).map(Amenity::getValue).toList();
            params.and("amenities", amenities.toArray());
        };

        log.info("hotel offer params: {}", params.toString());
        Hotel[] hotels = amadeusClient.referenceData.locations.hotels.byCity.get(params);

        String[] hotelIds = Arrays.stream(hotels)
                .map(Hotel::getHotelId)
                .limit(5)
                .toArray(String[]::new);

        Params offerParams = Params.with("hotelIds", String.join(",",hotelIds));
        offerParams.and("adults", requestDto.getGuests());
        offerParams.and("checkInDate", requestDto.getCheckInDate());
        offerParams.and("checkOutDate", requestDto.getCheckOutDate());
        offerParams.and("roomQuantity", requestDto.getRoomQuantity());
        offerParams.and("bestRateOnly", requestDto.isBestRateOnly());
//        offerParams.and("lang", requestDto.getLang() != null ? requestDto.getLang() : "EN");
        if(requestDto.getCurrency() != null) {
            offerParams.and("currency", requestDto.getCurrency());
        }
        if(requestDto.getResidenceCountry() != null) {
            offerParams.and("countryOfResidence", requestDto.getResidenceCountry());
        }
        if(requestDto.getPriceRange() != null) {
            offerParams.and("priceRange", requestDto.getPriceRange());
        }

        log.info("searching for hotel offers: {}", offerParams.toString());
        try {
            HotelOfferSearch[] hotelOffers = amadeusClient.shopping.hotelOffersSearch.get(offerParams);
            log.info("{} found hotel offers", hotelOffers.length);

            String json = gson.toJson(hotelOffers);
            HotelOfferResponse[] hotelOfferResponses = gson.fromJson(json, HotelOfferResponse[].class);

            if(requestDto.getCurrency() != null) {
                Arrays.stream(hotelOfferResponses)
                    .flatMap(
                    h -> h.getOffers().stream())
                    .forEach(o -> {
                        if(!requestDto.getCurrency().equals(o.getPrice().getCurrency())) {
                            Money convertedMoney = currencyService.exchangeCurrency(
                                o.getPrice().getCurrency(), requestDto.getCurrency(), o.getPrice().getTotal());

                            // TODO: create a custom dto with original price and converted price fields
                            o.getPrice().setCurrency(convertedMoney.getCurrency());
                            o.getPrice().setTotal(convertedMoney.getAmount());
                        }
                });
            }
            return Arrays.asList(hotelOfferResponses);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("something went wrong: {}", e.getMessage());
        }
        return new ArrayList<>();
    }

    public HotelOfferResponse getHotelOfferDetails(String hotelOfferId) throws ResponseException {
        HotelOfferSearch hotelOffer = amadeusClient.shopping.hotelOfferSearch(hotelOfferId).get();
        System.out.println("offer details: " + hotelOffer.toString());
        String json = gson.toJson(hotelOffer);
        HotelOfferResponse hotelOfferDetails = gson.fromJson(json, HotelOfferResponse.class);
        System.out.println("hot off deets: " + hotelOfferDetails);
        return hotelOfferDetails;
    }

    @Override
    public HotelReservation bookHotel(HotelBookingRequestDto dto) throws Exception {
//        HotelOrder response = amadeusClient.booking.hotelOrders.post(body);
//        return response.getResponse().getBody();
        JsonObject jsonObject = gson.toJsonTree(dto).getAsJsonObject();
        log.info("hotel booking request body : {}", jsonObject);
        HotelOrder hotelOrder = amadeusClient.booking.hotelOrders.post(jsonObject);
//        return hotelOrder;
        HotelReservation hotelReservation = saveHotelReservation(hotelOrder, dto);
        System.out.println("hotel reserv: " + hotelReservation.toString());
        String gsonJson = gson.toJson(hotelOrder);
        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readTree(gsonJson);
        return hotelReservation;
    }

    public HotelReservation saveHotelReservation(HotelOrder order, HotelBookingRequestDto request) {
        System.out.println("hotel order converting: "  + order.toString());
        HotelReservation hotelReservation = new HotelReservation();
        try {
            hotelReservation = hotelReservationMapper.toEntity(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String kcUserId = null;
        UserProfile userProfile = null;
        if(Helper.isUserAuthenticated()) {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(jwt != null) kcUserId = jwt.getClaim("sub");
            if(kcUserId != null) userProfile = userProfileService.findUserByKcUserId(kcUserId);
        }

        hotelReservation.setUserProfile(userProfile);
        hotelReservation.setKcUserId(kcUserId);

        Gson gson = new Gson();
        hotelReservation.setBookingRequest(gson.toJson(request));
        hotelReservation.setBookingResponse(gson.toJson(order));


        return hotelReservationRepository.save(hotelReservation);
    }
}

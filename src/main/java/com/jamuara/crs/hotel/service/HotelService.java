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
import com.jamuara.crs.enums.Amenity;
import com.jamuara.crs.hotel.dto.HotelSearchRequestDto;
import com.jamuara.crs.hotel.mappers.HotelSearchResponseMapper;
import com.jamuara.crs.hotel.model.HotelOfferResponse;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HotelService implements IHotelService {
    private final Amadeus amadeusClient;

    private HotelSearchResponseMapper searchResponseMapper;

    @Autowired
    private Gson gson;

    public HotelService(Amadeus amadeusClient, HotelSearchResponseMapper searchResponseMapper) {
        this.amadeusClient = amadeusClient;
        this.searchResponseMapper = searchResponseMapper;
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

        Hotel[] hotels = amadeusClient.referenceData.locations.hotels.byCity.get(params);

        String[] hotelIds = Arrays.stream(hotels)
                .map(Hotel::getHotelId)
                .limit(21)
                .toArray(String[]::new);

        Params offerParams = Params.with("hotelIds", Arrays.toString(hotelIds));
        offerParams.and("adults", requestDto.getGuests());
        offerParams.and("checkInDate", requestDto.getCheckInDate());
        offerParams.and("checkOutDate", requestDto.getCheckOutDate());
        offerParams.and("countryOfResidence", requestDto.getResidenceCountry());
        offerParams.and("roomQuantity", requestDto.getRoomsQuantity());
        if(requestDto.getPriceRange() != null) {
            offerParams.and("priceRange", requestDto.getRoomsQuantity());
            offerParams.and("currency", requestDto.getCurrency());
        }
        offerParams.and("bestRateOnly", requestDto.isBestRateOnly());
        offerParams.and("lang", requestDto.getLang() != null ? requestDto.getLang() : "EN");

        log.info("searching for hotel offers: {}", offerParams);
        HotelOfferSearch[] hotelOffers = amadeusClient.shopping.hotelOffersSearch.get(offerParams);
        log.info("found hotel offers: {}", Arrays.toString(hotelOffers));

        String json = gson.toJson(hotelOffers);
        HotelOfferResponse[] hotelOfferResponses = gson.fromJson(json, HotelOfferResponse[].class);

        return Arrays.asList(hotelOfferResponses);
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
    public JsonNode bookHotel(Map<String, Object> body) throws Exception {
//        HotelOrder response = amadeusClient.booking.hotelOrders.post(body);
//        return response.getResponse().getBody();
        JsonObject jsonObject = gson.toJsonTree(body).getAsJsonObject();
        System.out.println("body for booking: " + jsonObject);
        HotelOrder hotelOrder = amadeusClient.booking.hotelOrders.post(jsonObject);
//        return hotelOrder;
        String gsonJson = gson.toJson(hotelOrder);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(gsonJson);
    }
}

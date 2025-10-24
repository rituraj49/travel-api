package com.jamuara.crs.hotel.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.HotelOrder;
import com.google.gson.Gson;
import com.jamuara.crs.hotel.mappers.HotelSearchResponseMapper;
import com.jamuara.crs.hotel.model.HotelOfferResponse;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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

    @Override
    public String bookHotel(String body) throws Exception {
        HotelOrder response = amadeusClient.booking.hotelOrders.post(body);
        return response.getResponse().getBody();
    }
}

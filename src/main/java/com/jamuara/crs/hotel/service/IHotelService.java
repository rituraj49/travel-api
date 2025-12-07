package com.jamuara.crs.hotel.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.HotelOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.jamuara.crs.hotel.dto.HotelSearchRequestDto;
import com.jamuara.crs.hotel.model.HotelOfferResponse;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IHotelService {
    List<HotelSearchResponse> getHotels(String cityCode, Integer radius, String radiusUnit, List<String> amenities, List<String> ratings) throws Exception;
    List<HotelOfferResponse> getOffers(Map<String, String> paramsMap) throws Exception;
    JsonNode bookHotel(Map<String, Object> body) throws Exception;

    List<HotelOfferResponse> getHotelOffers(HotelSearchRequestDto requestDto) throws Exception;
    HotelOfferResponse getHotelOfferDetails(String hotelOfferId) throws Exception;
}

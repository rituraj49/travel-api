package com.jamuara.crs.hotel.service;

import com.amadeus.exceptions.ResponseException;
import com.jamuara.crs.hotel.model.HotelOfferResponse;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IHotelService {
    List<HotelSearchResponse> getHotels(String cityCode, Integer radius, String radiusUnit, List<String> amenities, List<String> ratings) throws Exception;
    List<HotelOfferResponse> getOffers(Map<String, String> paramsMap) throws Exception;
    String bookHotel(String body) throws Exception;
}

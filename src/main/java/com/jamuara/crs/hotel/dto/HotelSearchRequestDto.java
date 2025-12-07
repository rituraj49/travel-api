package com.jamuara.crs.hotel.dto;

import com.jamuara.crs.enums.Amenity;
import lombok.Data;

import java.util.List;

@Data
public class HotelSearchRequestDto {
    private String cityCode;
    private int radius;
    private String radiusUnit;
    private List<String> amenities;
    private int guests;
    private String checkInDate;
    private String checkOutDate;
    private int roomsQuantity;
    private String priceRange;
    private String currency;
    private boolean bestRateOnly;
    private String lang;
}

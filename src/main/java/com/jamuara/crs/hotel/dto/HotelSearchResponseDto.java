package com.jamuara.crs.hotel.dto;

import com.jamuara.crs.enums.HotelRateCode;
import lombok.Data;

import java.util.List;

@Data
public class HotelSearchResponseDto {
    private String hotelId;
    private String chainCode;
    private String dupeId;
    private String name;
    private String cityCode;
    private Double latitude;
    private Double longitude;
    private boolean available;
    private List<HotelOffer> offers;

    @Data
    public static class HotelOffer {
        private String id;
        private String checkInDate;
        private String checkOutDate;
        private int roomsQuantity;
        private HotelRateCode rateCode;

    }
}

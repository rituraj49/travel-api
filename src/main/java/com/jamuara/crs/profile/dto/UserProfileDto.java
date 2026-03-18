package com.jamuara.crs.profile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jamuara.crs.model.HotelReservation;
import com.jamuara.crs.model.Reservation;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private AddressDto address;
    private List<Reservation> reservations;
    private List<HotelReservation> hotelReservations;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AddressDto {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String country;
        private String zipCode;
    }
}

package com.jamuara.crs.profile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jamuara.crs.model.Reservation;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegisterDto {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String country;
        private String zipCode;
}

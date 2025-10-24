package com.jamuara.crs.profile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private AddressDto address;

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

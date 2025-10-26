package com.jamuara.crs.flight.dto.tbo.book;

import com.jamuara.crs.enums.Gender;
import com.jamuara.crs.enums.TravelerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TravelerRequestDto {

    private String title;

    @Schema(example = "Rahul")
    private String firstName;

    @Schema(example = "Sharma")
    private String lastName;

    @Schema(example = "1992-02-09")
    private String dateOfBirth;

    @Schema(example = "rahul@test.com")
    private String email;

    @Schema(example = "MALE")
    private Gender gender;

    private TravelerType travelerType;

    private boolean lead;

    private String phoneCountryCode;

    private String phone;

    private TravelerDto.IdentityDocument passportDetails;

    private TravelerDto.AddressDto address;

    @Data
    public static class AddressDto {
        private String line1;
        private String line2;
        private String city;
        private String country;
        private String countryCode;
    }

    @Data
    public static class IdentityDocument {
        @Schema(example = "AB1234567")
        private String number;

        @Schema(example = "2026-03-01")
        private String expiryDate;

        @Schema(example = "IN")
        private String nationality;
    }
}

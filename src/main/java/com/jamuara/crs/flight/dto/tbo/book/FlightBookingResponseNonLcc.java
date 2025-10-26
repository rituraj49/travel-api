package com.jamuara.crs.flight.dto.tbo.book;

import com.jamuara.crs.enums.Gender;
import com.jamuara.crs.enums.TravelerType;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FlightBookingResponseNonLcc {
    private String traceId;

    private BookingDetails bookingDetails;

    @Data
    public static class BookingDetails {
        private String pnr;
        private String bookingId;
        private BookingStatus bookingStatus;
        private boolean priceChanged;
        private boolean timeChanged;
        private BookFlightDetails flightDetails;
    }

    @Data
    public static class BookFlightDetails {
        private boolean LCC;

        private boolean nonRefundable;

        private boolean domestic;

        private String validatingAirline;

        private String airlineTollFreeNo;

        private String lastTicketDate;

        private Fare fare;

        private List<TravelerDto> travelers;

        private List<FlightDetailsResponse.FlightLeg> flightLegs;

        private String totalLayover;
    }

//    @Data
//    public static class TravelerDto {
//        private String id;
//
//        private String title;
//
//        @Schema(example = "Rahul")
//        private String firstName;
//
//        @Schema(example = "Sharma")
//        private String lastName;
//
//        @Schema(example = "1992-02-09")
//        private String dateOfBirth;
//
//        @Schema(example = "rahul@test.com")
//        private String email;
//
//        @Schema(example = "MALE")
//        private Gender gender;
//
//        private TravelerType travelerType;
//
//        private boolean lead;
//
////        private String phoneCountryCode;
//
//        private String phone;
//
//        private IdentityDocument passportDetails;
//
//        private AddressDto address;
//
//        private Fare farePerTraveler;
//    }

//    @Data
//    public static class TravelerFare {
//        private String currency;
//
//        private String baseFare;
//
//        private String tax;
//
//        private List<FlightDetailsResponse.TaxChargeBreakup> taxBreakup;
//
//        private String yqTax;
//
//        private String pgCharge;
//
//        private String otherCharges;
//
//        private List<FlightDetailsResponse.TaxChargeBreakup> chargesBreakup;
//
//        private String publishedFare;
//
//        private String serviceFee;
//    }

//    @Data
//    public static class AddressDto {
//        private String line1;
//        private String line2;
//        private String city;
//        private String country;
//        private String countryCode;
//    }
//
//    @Data
//    public static class IdentityDocument {
//        @Schema(example = "AB1234567")
//        private String number;
//
//        @Schema(example = "2026-03-01")
//        private String expiryDate;
//
//        @Schema(example = "IN")
//        private String nationality;
//    }

    @Data
    public static class Fare {
        private String currency;

        private String totalBaseFareAmount;

        private String totalTaxAmount;

        private List<FlightDetailsResponse.TaxChargeBreakup> taxBreakup;

        private String yqTax;

        private String pgCharge;

        private String otherCharges;

        private List<FlightDetailsResponse.TaxChargeBreakup> chargesBreakup;

        private String publishedFare; // sum of all taxes and service fee and other charges and base fare and GST and TDS and optional agency markup

        private String serviceFee;

        private String baggageCharges;

        private String mealCharges;

        private String seatCharges;

        private String specialServiceCharges;
    }

    public static enum BookingStatus {
        NotSet,
        Successful,
        Failed,
        OtherFare,
        OtherClass,
        BookedOther,
        NotConfirmed
    }
}

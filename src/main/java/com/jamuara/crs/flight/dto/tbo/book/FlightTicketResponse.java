package com.jamuara.crs.flight.dto.tbo.book;

import com.jamuara.crs.enums.Gender;
import com.jamuara.crs.enums.TicketStatus;
import com.jamuara.crs.enums.TravelerType;
import com.jamuara.crs.enums.TripType;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FlightTicketResponse {
    private String traceId;

    private TicketBookingDetails ticketBookingDetails;

    @Data
    public static class TicketBookingDetails {
        private String pnr;
        private String bookingId;
        private FlightBookingResponseNonLcc.BookingStatus bookingStatus;
        private TicketStatus ticketStatus;
        private boolean priceChanged;
        private boolean timeChanged;
        private TicketBookFlightDetails flightDetails;
    }

    @Data
    public static class TicketBookFlightDetails {
        private boolean LCC;

        public String issuancePcc;

        private boolean nonRefundable;

        private TripType tripType;

        private boolean domestic;

        private String validatingAirline;

        private String origin;

        private String destination;

        private String lastTicketDate;

        private String airlineTollFreeNo;

        private TicketFare ticketFare;

        private List<TicketTravelerDto> travelers;

        private List<FlightDetailsResponse.FlightLeg> flightLegs;

        private String totalLayover;

        private List<Invoice> invoice;

        private boolean webCheckInEligible;
    }

    @Data
    public static class Invoice {
        private String invoiceDate;

        private String invoiceId;

        private String invoiceNo;

        private String invoiceAmount;

//        private String invoiceStatus;

    }

    @Data
    public static class TicketFare {
        private String currency;

        private String totalBaseFareAmount;

        private String totalTaxAmount;

        private List<FlightDetailsResponse.TaxChargeBreakup> taxBreakup;

        private String yqTax;

        private String pgCharge;

        private String otherCharges;

        private List<FlightDetailsResponse.TaxChargeBreakup> chargesBreakup;

        private String publishedFare;

        private String serviceFee;

        private String baggageCharges;

        private String mealCharges;

        private String seatCharges;

        private String specialServiceCharges;
    }

    @Data
    public static class TicketTravelerDto {
        private String travelerId;

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

        private DocumentDetails documentDetails;

        private TravelerDto.AddressDto address;

        private TicketFare farePerTraveler;

        private BarcodeDetails barcodeDetails;

        private Ticket ticket;

        private AdditionalInfo additionalInfo;
    }

    @Data
    public static class BarcodeDetails {
        private String format;

        private String content;

        private String inBase64;
    }

    @Data
    public static class AdditionalInfo {
        private String fareBasisCode;

        private String notValidAfterDate;

        private String notValidBeforeDate;

        private String baggage;

        private String cabinBaggage;

        private String meal;

        private String specialService;
    }

    @Data
    public static class Ticket {
        private String ticketId;

        private String ticketNumber;

        private String issueDate;

        private String validatingAirline;
    }

    @Data
    public static class DocumentDetails {
        @Schema(example = "AB1234567")
        private String number;

        @Schema(example = "2026-03-01")
        private String expiryDate;

        private String documentType;

        private String travelerId;
    }
}

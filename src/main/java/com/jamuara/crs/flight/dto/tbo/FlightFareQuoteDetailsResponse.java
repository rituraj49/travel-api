package com.jamuara.crs.flight.dto.tbo;

import com.jamuara.crs.enums.TravelClass;
import com.jamuara.crs.enums.TripType;
import lombok.Data;

import java.util.List;

@Data
public class FlightFareQuoteDetailsResponse {
    private String resultIndex;

    private boolean LCC;

    private boolean refundable;

    private boolean passportRequiredAtBook;

    private boolean passportRequiredAtTicket;

    private String currency;

    private String lastTicketDate;

    private String totalBaseFareAmount;

    private String totalTaxAmount;

    private List<TaxChargeBreakup> taxBreakup;

    private String yqTax;

    private String pgCharge;

    private String otherCharges;

    private List<TaxChargeBreakup> chargesBreakup;

    private String publishedFare;

    private String offeredFare;

    private String commissionEarned;

    private String incentiveEarned;

    private String plbEarned;

    private String totalBaggageCharges;

    private String totalMealCharges;

    private String totalSeatCharges;

    private List<FareDetails> travelerDetails;

    private TripType tripType;

    private List<FlightLeg> flightLegs;

    private String validatingAirline;

    private String airlineRemark;

    private String fareType;

    private String fareColor;

    private String totalLayover;

    @Data
    public static class TaxChargeBreakup {
        private String key;
        private String value;
    }

    @Data
    public static class FareDetails {
        private TravelerType travelerType;
        private int travelersCount;
        private String baseFare;
        private String tax;
        private List<TaxChargeBreakup> taxBreakup;
        private String yqTax;
        private String pgCharge;
    }

    @Data
    public static class FlightLeg {
        private int legNo;
        private int tripNo;
//        private TripType tripType;
        private String baggage;
        private String cabinBaggage;
        private TravelClass cabinClass;

        private String carrierCode;
        private String carrierName;
        private String operatingCarrier;
        private String flightNumber;
        private String aircraftCode;

        private String departureAirport;
        private String departureAirportName;
        private String departureTerminal;
        private String departureCityName;
        private String departureCountryName;
        private String departureDateTime;

        private String arrivalAirport;
        private String arrivalAirportName;
        private String arrivalTerminal;
        private String arrivalCityName;
        private String arrivalCountryName;
        private String arrivalDateTime;

        private String duration;
        private String layoverDuration;
        private String fareBasisCode;
    }

    public enum TravelerType {
        ADULT,
        CHILD,
        INFANT
    }
}



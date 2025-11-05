package com.jamuara.crs.flight.dto.tbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TboApiFareQuoteResponseDto {
    @JsonProperty("Response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("Error")
        private Error error;

        @JsonProperty("FlightDetailChangeInfo")
        private String flightDetailChangeInfo;

        @JsonProperty("IsPriceChanged")
        private boolean isPriceChanged;
        // private boolean priceChanged;

        @JsonProperty("ItineraryChangeList")
        private String[] itineraryChangeList;

        @JsonProperty("ResponseStatus")
        private int responseStatus;

        @JsonProperty("TraceId")
        private String traceId;

        @JsonProperty("Results")
        private Results results;

    }

    @Data
    public static class Error {
        @JsonProperty("ErrorCode")
        private int errorCode;

        @JsonProperty("ErrorMessage")
        private String errorMessage;
    }

    @Data
    public static class Results {
        @JsonProperty("ResultIndex")
        private String resultIndex;

        @JsonProperty("IsLCC")
        private boolean isLCC;

        @JsonProperty("IsRefundable")
        private boolean isRefundable;

        @JsonProperty("AirlineRemark")
        private String airlineRemark;

        @JsonProperty("Fare")
        private Fare fare;

        @JsonProperty("FareBreakdown")
        private List<FareBreakdown> fareBreakdown;

        @JsonProperty("Segments")
        private List<List<Segment>> segments;

        @JsonProperty("FareRules")
        private List<FareRule> fareRules;

        @JsonProperty("ValidatingAirline")
        private String validatingAirline;

        @JsonProperty("LastTicketDate")
        private String lastTicketDate;

        @JsonProperty("FareClassification")
        private FareClassification fareClassification;
    }

    @Data
    public static class Fare {
        @JsonProperty("Currency")
        private String currency;

        @JsonProperty("BaseFare")
        private double baseFare;

        @JsonProperty("Tax")
        private double tax;

        @JsonProperty("TaxBreakup")
        private List<KeyValue> taxBreakup;

        @JsonProperty("PublishedFare")
        private double publishedFare;

        @JsonProperty("OfferedFare")
        private double offeredFare;

        @JsonProperty("YQTax")
        private double yqTax;

        @JsonProperty("PGCharge")
        private double pgCharge;

        @JsonProperty("OtherCharges")
        private double otherCharges;

        @JsonProperty("ChargeBU")
        private List<KeyValue> chargeBU;

        @JsonProperty("CommissionEarned")
        private double commissionEarned;

        @JsonProperty("PLBEarned")
        private double plbEarned;

        @JsonProperty("IncentiveEarned")
        private double incentiveEarned;

        @JsonProperty("TotalBaggageCharges")
        private double totalBaggageCharges;

        @JsonProperty("TotalMealCharges")
        private double totalMealCharges;

        @JsonProperty("TotalSeatCharges")
        private double totalSeatCharges;
    }

    @Data
    public static class KeyValue {
        @JsonProperty("key")
        private String key;

        @JsonProperty("value")
        private double value;
    }

    @Data
    public static class FareBreakdown {
        @JsonProperty("Currency")
        private String currency;

        @JsonProperty("PassengerType")
        private int passengerType;

        @JsonProperty("PassengerCount")
        private int passengerCount;

        @JsonProperty("BaseFare")
        private double baseFare;

        @JsonProperty("Tax")
        private double tax;

        @JsonProperty("TaxBreakUp")
        private List<KeyValue> taxBreakUp;

        @JsonProperty("YQTax")
        private double yqTax;

        @JsonProperty("PGCharge")
        private double pgCharge;
    }

    @Data
    public static class Segment {
        @JsonProperty("Baggage")
        private String baggage;

        @JsonProperty("CabinBaggage")
        private String cabinBaggage;

        @JsonProperty("CabinClass")
        private int cabinClass;

        @JsonProperty("SupplierFareClass")
        private String supplierFareClass;

        @JsonProperty("TripIndicator")
        private int tripIndicator;

        @JsonProperty("SegmentIndicator")
        private int segmentIndicator;

        @JsonProperty("Airline")
        private Airline airline;

        @JsonProperty("Origin")
        private AirportInfo origin;

        @JsonProperty("Destination")
        private AirportInfo destination;

        @JsonProperty("Duration")
        private int duration;

        @JsonProperty("GroundTime")
        private int groundTime;

        @JsonProperty("Mile")
        private int mile;

        @JsonProperty("StopOver")
        private boolean stopOver;

        @JsonProperty("FlightInfoIndex")
        private String flightInfoIndex;

        @JsonProperty("StopPoint")
        private String stopPoint;

        @JsonProperty("StopPointArrivalTime")
        private String stopPointArrivalTime;

        @JsonProperty("StopPointDepartureTime")
        private String stopPointDepartureTime;

        @JsonProperty("Craft")
        private String craft;

        @JsonProperty("Remark")
        private String remark;

        @JsonProperty("IsETicketEligible")
        private boolean isETicketEligible;

        @JsonProperty("FlightStatus")
        private String flightStatus;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("FareClassification")
        private String fareClassification;
    }

    @Data
    public static class Airline {
        @JsonProperty("AirlineCode")
        private String airlineCode;

        @JsonProperty("AirlineName")
        private String airlineName;

        @JsonProperty("FlightNumber")
        private String flightNumber;

        @JsonProperty("FareClass")
        private String fareClass;

        @JsonProperty("OperatingCarrier")
        private String operatingCarrier;
    }

    @Data
    public static class AirportInfo {
        @JsonProperty("Airport")
        private Airport airport;

        @JsonProperty("DepTime")
        private String depTime;

        @JsonProperty("ArrTime")
        private String arrTime;
    }

    @Data
    public static class Airport {
        @JsonProperty("AirportCode")
        private String airportCode;

        @JsonProperty("AirportName")
        private String airportName;

        @JsonProperty("Terminal")
        private String terminal;

        @JsonProperty("CityCode")
        private String cityCode;

        @JsonProperty("CityName")
        private String cityName;

        @JsonProperty("CountryCode")
        private String countryCode;

        @JsonProperty("CountryName")
        private String countryName;
    }

    @Data
    public static class FareRule {
        @JsonProperty("FareBasisCode")
        private String fareBasisCode;

        @JsonProperty("FareRuleDetail")
        private String fareRuleDetail;

        @JsonProperty("FareRestriction")
        private String fareRestriction;

        @JsonProperty("Origin")
        private String origin;

        @JsonProperty("Destination")
        private String destination;
    }

    @Data
    public static class FareClassification {
        @JsonProperty("Color")
        private String color;

        @JsonProperty("Type")
        private String type;
    }
}

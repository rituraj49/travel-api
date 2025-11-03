package com.jamuara.crs.flight.dto.tbo.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TBOGetBookingDetailsResponse {

    @JsonProperty("Response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("Error")
        private Error error;

        @JsonProperty("ResponseStatus")
        private int responseStatus;

        @JsonProperty("TraceId")
        private String traceId;

        @JsonProperty("FlightItinerary")
        private FlightItinerary flightItinerary;
    }

    @Data
    public static class Error {
        @JsonProperty("ErrorCode")
        private int errorCode;

        @JsonProperty("ErrorMessage")
        private String errorMessage;
    }

    @Data
    public static class FlightItinerary {

        @JsonProperty("FareClassification")
        private String fareClassification;

        @JsonProperty("IsAutoReissuanceAllowed")
        private boolean isAutoReissuanceAllowed;

        @JsonProperty("IsSeatsBooked")
        private boolean isSeatsBooked;

        @JsonProperty("JourneyType")
        private int journeyType;

        @JsonProperty("PNR")
        private String pnr;

        @JsonProperty("IsDomestic")
        private boolean isDomestic;

        @JsonProperty("ResultFareType")
        private String resultFareType;

        @JsonProperty("Origin")
        private String origin;

        @JsonProperty("Destination")
        private String destination;

        @JsonProperty("AirlineCode")
        private String airlineCode;

        @JsonProperty("LastTicketDate")
        private String lastTicketDate;

        @JsonProperty("NonRefundable")
        private boolean nonRefundable;

        @JsonProperty("Fare")
        private Fare fare;

        @JsonProperty("Passenger")
        private List<Passenger> passenger;

        @JsonProperty("Segments")
        private List<Segment> segments;

        @JsonProperty("FareRules")
        private List<FareRule> fareRules;

        @JsonProperty("Invoice")
        private List<Invoice> invoice;
    }

    @Data
    public static class Fare {
        @JsonProperty("Currency")
        private String currency;

        @JsonProperty("BaseFare")
        private double baseFare;

        @JsonProperty("Tax")
        private double tax;

        @JsonProperty("PublishedFare")
        private double publishedFare;

        @JsonProperty("OfferedFare")
        private double offeredFare;

        @JsonProperty("CommissionEarned")
        private double commissionEarned;

        @JsonProperty("IncentiveEarned")
        private double incentiveEarned;

        @JsonProperty("PLBEarned")
        private double plbEarned;

        @JsonProperty("TaxBreakup")
        private List<Map<String, Object>> taxBreakup;
    }

    @Data
    public static class Passenger {
        @JsonProperty("Title")
        private String title;

        @JsonProperty("FirstName")
        private String firstName;

        @JsonProperty("LastName")
        private String lastName;

        @JsonProperty("PaxType")
        private int paxType;

        @JsonProperty("DateOfBirth")
        private String dateOfBirth;

        @JsonProperty("Gender")
        private String gender;

        @JsonProperty("PassportNo")
        private String passportNo;

        @JsonProperty("City")
        private String city;

        @JsonProperty("CountryCode")
        private String countryCode;

        @JsonProperty("Nationality")
        private String nationality;

        @JsonProperty("ContactNo")
        private String contactNo;

        @JsonProperty("Email")
        private String email;

        @JsonProperty("IsLeadPax")
        private boolean isLeadPax;

        @JsonProperty("Fare")
        private Fare fare;

        @JsonProperty("Ticket")
        private Ticket ticket;

        @JsonProperty("SegmentAdditionalInfo")
        private List<SegmentAdditionalInfo> segmentAdditionalInfo;
    }

    @Data
    public static class Ticket {
        @JsonProperty("TicketNumber")
        private String ticketNumber;

        @JsonProperty("IssueDate")
        private String issueDate;

        @JsonProperty("Status")
        private String status;
    }

    @Data
    public static class SegmentAdditionalInfo {
        @JsonProperty("Baggage")
        private String baggage;

        @JsonProperty("Meal")
        private String meal;

        @JsonProperty("CabinBaggage")
        private String cabinBaggage;
    }

    @Data
    public static class Segment {
        @JsonProperty("CabinClass")
        private int cabinClass;

        @JsonProperty("Duration")
        private int duration;

        @JsonProperty("Airline")
        private Airline airline;

        @JsonProperty("Origin")
        private Origin origin;

        @JsonProperty("Destination")
        private Destination destination;
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
    }

    @Data
    public static class Origin {
        @JsonProperty("Airport")
        private Airport airport;

        @JsonProperty("DepTime")
        private String depTime;
    }

    @Data
    public static class Destination {
        @JsonProperty("Airport")
        private Airport airport;

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
        @JsonProperty("Origin")
        private String origin;

        @JsonProperty("Destination")
        private String destination;

        @JsonProperty("Airline")
        private String airline;

        @JsonProperty("FareBasisCode")
        private String fareBasisCode;

        @JsonProperty("FareRuleDetail")
        private String fareRuleDetail;

        @JsonProperty("FareInclusions")
        private List<String> fareInclusions;
    }

    @Data
    public static class Invoice {
        @JsonProperty("InvoiceNo")
        private String invoiceNo;

        @JsonProperty("InvoiceAmount")
        private double invoiceAmount;

        @JsonProperty("InvoiceCreatedOn")
        private String invoiceCreatedOn;
    }
}

package com.jamuara.crs.flight.dto.tbo.book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TboApiFlightBookingResponseDto {

    @JsonProperty("Response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("Error")
        private ErrorDetail error;

        @JsonProperty("TraceId")
        private String traceId;

        @JsonProperty("ResponseStatus")
        private int responseStatus;

        @JsonProperty("Response")
        private ResponseData response;

        @Data
        public static class ErrorDetail {
            @JsonProperty("ErrorCode")
            private int errorCode;

            @JsonProperty("ErrorMessage")
            private String errorMessage;
        }

        @Data
        public static class ResponseData {
            @JsonProperty("PNR")
            private String pnr;

            @JsonProperty("BookingId")
            private int bookingId;

            @JsonProperty("SSRDenied")
            private boolean ssrDenied;

            @JsonProperty("SSRMessage")
            private String ssrMessage;

            @JsonProperty("Status")
            private int status;

            @JsonProperty("IsPriceChanged")
            private boolean priceChanged;

            @JsonProperty("IsTimeChanged")
            private boolean timeChanged;

            @JsonProperty("FlightItinerary")
            private FlightItinerary flightItinerary;
        }

        @Data
        public static class FlightItinerary {
            @JsonProperty("BookingAllowedForRoamer")
            private boolean bookingAllowedForRoamer;

            @JsonProperty("BookingId")
            private int bookingId;

            @JsonProperty("PNR")
            private String pnr;

            @JsonProperty("IsManual")
            private boolean isManual;

            @JsonProperty("Source")
            private int source;

            @JsonProperty("IsDomestic")
            private boolean domestic;

            @JsonProperty("Origin")
            private String origin;

            @JsonProperty("Destination")
            private String destination;

            @JsonProperty("AirlineCode")
            private String airlineCode;

            @JsonProperty("ValidatingAirlineCode")
            private String validatingAirlineCode;

            @JsonProperty("LastTicketDate")
            private String lastTicketDate;

            @JsonProperty("AirlineTollFreeNo")
            private String airlineTollFreeNo;

            @JsonProperty("IsLCC")
            private boolean LCC;

            @JsonProperty("NonRefundable")
            private boolean nonRefundable;

            @JsonProperty("AirlineRemark")
            private String airlineRemark;

            @JsonProperty("FareType")
            private String fareType;

            @JsonProperty("CreditNoteNo")
            private String creditNoteNo;

            @JsonProperty("BaseCurrencyFare")
            private Fare baseCurrencyFare;

            @JsonProperty("Fare")
            private Fare fare;

            @JsonProperty("CreditNoteCreatedOn")
            private String creditNoteCreatedOn;

            @JsonProperty("Passenger")
            private List<Passenger> passengers;

            @JsonProperty("CancellationCharges")
            private Object cancellationCharges;

            @JsonProperty("Segments")
            private List<Segment> segments;

            @JsonProperty("FareRules")
            private List<FareRule> fareRules;

            @JsonProperty("Status")
            private int status;

            @JsonProperty("BookingHistory")
            private List<BookingHistory> bookingHistory;
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
            private List<TaxBreakup> taxBreakup;

            @JsonProperty("YQTax")
            private double yqTax;

            @JsonProperty("AdditionalTxnFeePub")
            private double additionalTxnFeePub;

            @JsonProperty("PGCharge")
            private double pgCharge;

            @JsonProperty("AdditionalTxnFeeOfrd")
            private double additionalTxnFeeOfrd;

            @JsonProperty("OtherCharges")
            private double otherCharges;

            @JsonProperty("ChargeBU")
            private List<ChargeBU> chargeBU;

            @JsonProperty("Discount")
            private double discount;

            @JsonProperty("PublishedFare")
            private double publishedFare;

            @JsonProperty("CommissionEarned")
            private double commissionEarned;

            @JsonProperty("PLBEarned")
            private double plbEarned;

            @JsonProperty("IncentiveEarned")
            private double incentiveEarned;

            @JsonProperty("OfferedFare")
            private double offeredFare;

            @JsonProperty("TdsOnCommission")
            private double tdsOnCommission;

            @JsonProperty("TdsOnPLB")
            private double tdsOnPlb;

            @JsonProperty("TdsOnIncentive")
            private double tdsOnIncentive;

            @JsonProperty("ServiceFee")
            private double serviceFee;

            @JsonProperty("TotalBaggageCharges")
            private double totalBaggageCharges;

            @JsonProperty("TotalMealCharges")
            private double totalMealCharges;

            @JsonProperty("TotalSeatCharges")
            private double totalSeatCharges;

            @JsonProperty("TotalSpecialServiceCharges")
            private double totalSpecialServiceCharges;
        }

        @Data
        public static class TaxBreakup {
            @JsonProperty("Key")
            private String key;

            @JsonProperty("Value")
            private double value;
        }

        @Data
        public static class ChargeBU {
            @JsonProperty("Key")
            private String key;

            @JsonProperty("Value")
            private double value;
        }

        @Data
        public static class Passenger {
            @JsonProperty("PaxId")
            private long paxId;

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
            private int gender;

            @JsonProperty("PassportNo")
            private String passportNo;

            @JsonProperty("PassportExpiry")
            private String passportExpiry;

            @JsonProperty("AddressLine1")
            private String addressLine1;

            @JsonProperty("AddressLine2")
            private String addressLine2;

            @JsonProperty("Fare")
            private Fare fare;

            @JsonProperty("City")
            private String city;

            @JsonProperty("CountryCode")
            private String countryCode;

            @JsonProperty("CountryName")
            private String countryName;

            @JsonProperty("Nationality")
            private String nationality;

            @JsonProperty("CellCountryCode")
            private String cellCountryCode;

            @JsonProperty("ContactNo")
            private String contactNo;

            @JsonProperty("Email")
            private String email;

            @JsonProperty("IsLeadPax")
            private boolean leadPax;

            @JsonProperty("FFAirlineCode")
            private String ffAirlineCode;

            @JsonProperty("FFNumber")
            private String ffNumber;

            @JsonProperty("GSTCompanyAddress")
            private String gstCompanyAddress;

            @JsonProperty("GSTCompanyContactNumber")
            private String gstCompanyContactNumber;

            @JsonProperty("GSTCompanyName")
            private String gstCompanyName;

            @JsonProperty("GSTNumber")
            private String gstNumber;

            @JsonProperty("GSTCompanyEmail")
            private String gstCompanyEmail;
        }

        @Data
        public static class Segment {
            @JsonProperty("Baggage")
            private String baggage;

            @JsonProperty("CabinBaggage")
            private String cabinBaggage;

            @JsonProperty("TripIndicator")
            private int tripIndicator;

            @JsonProperty("SegmentIndicator")
            private int segmentIndicator;

            @JsonProperty("Airline")
            private Airline airline;

            @JsonProperty("AirlinePNR")
            private String airlinePNR;

            @JsonProperty("Origin")
            private AirportDetail origin;

            @JsonProperty("Destination")
            private AirportDetail destination;

            @JsonProperty("Duration")
            private int duration;

            @JsonProperty("GroundTime")
            private int groundTime;

            @JsonProperty("Mile")
            private int mile;

            @JsonProperty("StopOver")
            private boolean stopOver;

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
        public static class AirportDetail {
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

            @JsonProperty("FareRestriction")
            private String fareRestriction;
        }

        @Data
        public static class BookingHistory {
            @JsonProperty("BookingId")
            private int bookingId;

            @JsonProperty("EventCategory")
            private int eventCategory;

            @JsonProperty("Remarks")
            private String remarks;

            @JsonProperty("CreatedOn")
            private String createdOn;

            @JsonProperty("CreatedBy")
            private int createdBy;

            @JsonProperty("CreatedByName")
            private String createdByName;

            @JsonProperty("LastModifiedOn")
            private String lastModifiedOn;

            @JsonProperty("LastModifiedBy")
            private int lastModifiedBy;

            @JsonProperty("LastModifiedByName")
            private String lastModifiedByName;
        }
    }

}

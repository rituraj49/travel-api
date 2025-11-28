package com.jamuara.crs.flight.dto.tbo.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TboApiFetchFlightBookingResponseDto {

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

        @Data
        public static class Error {
            @JsonProperty("ErrorCode")
            private int errorCode;

            @JsonProperty("ErrorMessage")
            private String errorMessage;
        }

        @Data
        public static class FlightItinerary {

            @JsonProperty("AgentRemarks")
            private String agentRemarks;

            @JsonProperty("CommentDetails")
            private Object commentDetails;

            @JsonProperty("FareClassification")
            private String fareClassification;

            @JsonProperty("IsSeatsBooked")
            private boolean isSeatsBooked;

            @JsonProperty("JourneyType")
            private int journeyType;

            @JsonProperty("SearchCombinationType")
            private int searchCombinationType;

            @JsonProperty("SupplierFareClasses")
            private String supplierFareClasses;

            @JsonProperty("TripIndicator")
            private int tripIndicator;

            @JsonProperty("BookingAllowedForRoamer")
            private boolean bookingAllowedForRoamer;

            @JsonProperty("BookingId")
            private int bookingId;

            @JsonProperty("IsCouponAppilcable")
            private boolean isCouponAppilcable;

            @JsonProperty("IsManual")
            private boolean isManual;

            @JsonProperty("PNR")
            private String pnr;

            @JsonProperty("IsDomestic")
            private boolean isDomestic;

            @JsonProperty("ResultFareType")
            private String resultFareType;

            @JsonProperty("Source")
            private int source;

            @JsonProperty("Origin")
            private String origin;

            @JsonProperty("Destination")
            private String destination;

            @JsonProperty("AirlineCode")
            private String airlineCode;

            @JsonProperty("LastTicketDate")
            private String lastTicketDate;

            @JsonProperty("ValidatingAirlineCode")
            private String validatingAirlineCode;

            @JsonProperty("AirlineRemark")
            private String airlineRemark;

            @JsonProperty("AirlineTollFreeNo")
            private String airlineTollFreeNo;

            @JsonProperty("IsLCC")
            private boolean LCC;

            @JsonProperty("NonRefundable")
            private boolean nonRefundable;

            @JsonProperty("FareType")
            private String fareType;

            @JsonProperty("CreditNoteNo")
            private Object creditNoteNo;

            @JsonProperty("Fare")
            private Fare fare;

            @JsonProperty("CreditNoteCreatedOn")
            private Object creditNoteCreatedOn;

            @JsonProperty("Passenger")
            private List<Passenger> passenger;

            @JsonProperty("CancellationCharges")
            private Object cancellationCharges;

            @JsonProperty("Segments")
            private List<Segment> segments;

            @JsonProperty("FareRules")
            private List<FareRule> fareRules;

            @JsonProperty("MiniFareRules")
            private List<MiniFareRule> miniFareRules;

            @JsonProperty("PenaltyCharges")
            private PenaltyCharges penaltyCharges;

            @JsonProperty("Status")
            private int status;

            @JsonProperty("IsWebCheckInAllowed")
            private boolean isWebCheckInAllowed;
        }

        @Data
        public static class BarcodeDetails {
            private String format;

            private String content;

            private String inBase64;
        }

        @Data
        public static class Fare {
            @JsonProperty("ServiceFeeDisplayType")
            private int serviceFeeDisplayType;

            @JsonProperty("Currency")
            private String currency;

            @JsonProperty("BaseFare")
            private double baseFare;

            @JsonProperty("Tax")
            private double tax;

            @JsonProperty("TaxBreakup")
            private List<KeyValue> taxBreakup;

            @JsonProperty("YQTax")
            private double yqTax;

            @JsonProperty("AdditionalTxnFeeOfrd")
            private double additionalTxnFeeOfrd;

            @JsonProperty("AdditionalTxnFeePub")
            private double additionalTxnFeePub;

            @JsonProperty("PGCharge")
            private double pgCharge;

            @JsonProperty("OtherCharges")
            private double otherCharges;

            @JsonProperty("ChargeBU")
            private List<KeyValue> chargeBu;

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

            @Data
            public static class KeyValue {
                @JsonProperty("key")
                private String key;

                @JsonProperty("value")
                private double value;
            }
        }

        @Data
        public static class Passenger {

            @JsonProperty("BarcodeDetails")
            private BarcodeDetails barcodeDetails;

            @JsonProperty("DocumentDetails")
            private List<DocumentDetail> documentDetails;

            @JsonProperty("GuardianDetails")
            private Object guardianDetails;

            @JsonProperty("PaxId")
            private int paxId;

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

            @JsonProperty("IsPANRequired")
            private boolean isPanRequired;

            @JsonProperty("IsPassportRequired")
            private boolean isPassportRequired;

            @JsonProperty("PAN")
            private String pan;

            @JsonProperty("PassportNo")
            private String passportNo;

            @JsonProperty("AddressLine1")
            private String addressLine1;

            @JsonProperty("Fare")
            private Fare fare;

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

            @JsonProperty("FFAirlineCode")
            private Object ffAirlineCode;

            @JsonProperty("FFNumber")
            private Object ffNumber;

            @JsonProperty("Ssr")
            private List<Ssr> ssr;

            @Data
            public static class DocumentDetail {
                @JsonProperty("DocumentExpiryDate")
                private String documentExpiryDate;

                @JsonProperty("DocumentIssueDate")
                private String documentIssueDate;

                @JsonProperty("DocumentIssuingCountry")
                private String documentIssuingCountry;

                @JsonProperty("DocumentNumber")
                private String documentNumber;

                @JsonProperty("DocumentTypeId")
                private String documentTypeId;

                @JsonProperty("PaxId")
                private int paxId;

                @JsonProperty("ResultFareType")
                private int resultFareType;
            }

            @Data
            public static class Ssr {
                @JsonProperty("Detail")
                private String detail;

                @JsonProperty("SsrCode")
                private String ssrCode;

                @JsonProperty("SsrStatus")
                private Object ssrStatus;

                @JsonProperty("Status")
                private int status;
            }
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
            private Object supplierFareClass;

            @JsonProperty("TripIndicator")
            private int tripIndicator;

            @JsonProperty("SegmentIndicator")
            private int segmentIndicator;

            @JsonProperty("Airline")
            private Airline airline;

            @JsonProperty("AirlinePNR")
            private String airlinePnr;

            @JsonProperty("Origin")
            private Origin origin;

            @JsonProperty("Destination")
            private Destination destination;

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
            private Object stopPointArrivalTime;

            @JsonProperty("StopPointDepartureTime")
            private Object stopPointDepartureTime;

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

            @JsonProperty("DepTime")
            private String depTime;

            @JsonProperty("ArrTime")
            private String arrTime;
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

            @JsonProperty("FareFamilyCode")
            private String fareFamilyCode;

            @JsonProperty("FareRuleIndex")
            private String fareRuleIndex;

            @JsonProperty("FareInclusions")
            private List<String> fareInclusions;
        }

        @Data
        public static class MiniFareRule {
            @JsonProperty("JourneyPoints")
            private String journeyPoints;

            @JsonProperty("Type")
            private String type;

            @JsonProperty("From")
            private String from;

            @JsonProperty("To")
            private String to;

            @JsonProperty("Unit")
            private String unit;

            @JsonProperty("Details")
            private String details;

            @JsonProperty("OnlineReissueAllowed")
            private boolean onlineReissueAllowed;

            @JsonProperty("OnlineRefundAllowed")
            private boolean onlineRefundAllowed;
        }

        @Data
        public static class PenaltyCharges {
            @JsonProperty("ReissueCharge")
            private String reissueCharge;

            @JsonProperty("CancellationCharge")
            private String cancellationCharge;
        }
    }
}

package com.jamuara.crs.flight.dto.tbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TboApiFlightResponseDto {

    @JsonProperty("Response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("ResultRecommendationType")
        private int resultRecommendationType;

        @JsonProperty("ResponseStatus")
        private int responseStatus;

        @JsonProperty("Error")
        private Error error;

        @JsonProperty("TraceId")
        private String traceId;

        @JsonProperty("Origin")
        private String origin;

        @JsonProperty("Destination")
        private String destination;

        @JsonProperty("Results")
        private List<List<Result>> results;

        @Data
        public static class Error {
            @JsonProperty("ErrorCode")
            private int errorCode;

            @JsonProperty("ErrorMessage")
            private String errorMessage;

            // getters and setters
        }

        @Data
        public static class Result {
            @JsonProperty("FareCombinationId")
            private String fareCombinationId;

            @JsonProperty("FareInclusions")
            private List<String> fareInclusions;

            @JsonProperty("FirstNameFormat")
            private String firstNameFormat;

            @JsonProperty("IsBookableIfSeatNotAvailable")
            private boolean isBookableIfSeatNotAvailable;

            @JsonProperty("IsExclusiveFare")
            private boolean isExclusiveFare;

            @JsonProperty("IsFreeMealAvailable")
            private boolean isFreeMealAvailable;

            @JsonProperty("IsHoldAllowedWithSSR")
            private boolean isHoldAllowedWithSSR;

            @JsonProperty("IsHoldMandatoryWithSSR")
            private boolean isHoldMandatoryWithSSR;

            @JsonProperty("LastNameFormat")
            private String lastNameFormat;

            @JsonProperty("NonStopFirstRanking")
            private int nonStopFirstRanking;

            @JsonProperty("SmartChoiceRanking")
            private int smartChoiceRanking;

            @JsonProperty("ResultIndex")
            private String resultIndex;

            @JsonProperty("Source")
            private int source;

            @JsonProperty("IsLCC")
            private boolean LCC;

            @JsonProperty("IsRefundable")
            private boolean refundable;

            @JsonProperty("IsPanRequiredAtBook")
            private boolean panRequiredAtBook;

            @JsonProperty("IsPanRequiredAtTicket")
            private boolean panRequiredAtTicket;

            @JsonProperty("IsPassportRequiredAtBook")
            private boolean isPassportRequiredAtBook;

            @JsonProperty("IsPassportRequiredAtTicket")
            private boolean isPassportRequiredAtTicket;

            @JsonProperty("GSTAllowed")
            private boolean gstAllowed;

            @JsonProperty("IsCouponAppilcable")
            private boolean isCouponApplicable;

            @JsonProperty("IsGSTMandatory")
            private boolean isGSTMandatory;

            @JsonProperty("AirlineRemark")
            private String airlineRemark;

            @JsonProperty("IsPassportFullDetailRequiredAtBook")
            private boolean isPassportFullDetailRequiredAtBook;

            @JsonProperty("ResultFareType")
            private String resultFareType;

            @JsonProperty("Fare")
            private Fare fare;

            @JsonProperty("FareBreakdown")
            private List<FareBreakdown> fareBreakdown;

            @JsonProperty("Segments")
            private List<List<Segment>> segments;

            @JsonProperty("LastTicketDate")
            private String lastTicketDate;

            @JsonProperty("TicketAdvisory")
            private String ticketAdvisory;

            @JsonProperty("FareRules")
            private List<FareRule> fareRules;

            @JsonProperty("AirlineCode")
            private String airlineCode;

            @JsonProperty("ValidatingAirline")
            private String validatingAirline;

            @JsonProperty("FareClassification")
            private FareClassification fareClassification;

            @JsonProperty("MiniFareRules")
            private List<List<MiniFareRule>> miniFareRules;

            // getters and setters
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
            private List<KeyValue> chargeBU;

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
            private double tdsOnPLB;

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

            // getters and setters
        }

        @Data
        public static class KeyValue {
            @JsonProperty("key")
            private String key;

            @JsonProperty("value")
            private double value;

            // getters and setters
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

            @JsonProperty("AdditionalTxnFeeOfrd")
            private double additionalTxnFeeOfrd;

            @JsonProperty("AdditionalTxnFeePub")
            private double additionalTxnFeePub;

            @JsonProperty("PGCharge")
            private double pgCharge;

            @JsonProperty("SupplierReissueCharges")
            private double supplierReissueCharges;
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

            @JsonProperty("NoOfSeatAvailable")
            private int noOfSeatAvailable;

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

            @JsonProperty("FareFamilyCode")
            private String fareFamilyCode;

            @JsonProperty("FareRuleIndex")
            private String fareRuleIndex;

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

        @Data
        public static class MiniFareRule {
            @JsonProperty("FareRuleType")
            private String fareRuleType;

            @JsonProperty("FareType")
            private String fareType;

            @JsonProperty("Content")
            private String content;

            @JsonProperty("ContentHtml")
            private String contentHtml;
        }
    }
}

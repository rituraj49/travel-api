package com.jamuara.crs.flight.dto.tbo.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TboApiFlightTicketResponseDto {
    @JsonProperty("Response")
    private BookingResponseWrapper response;

    @Data
    public static class BookingResponseWrapper {
        @JsonProperty("B2B2BStatus")
        private boolean b2b2bStatus;

        @JsonProperty("Error")
        private ErrorResponse error;

        @JsonProperty("ResponseStatus")
        private int responseStatus;

        @JsonProperty("TraceId")
        private String traceId;

        @JsonProperty("Response")
        private BookingResponseDetails response;
    }

    @Data
    public static class ErrorResponse {
        @JsonProperty("ErrorCode")
        private int errorCode;

        @JsonProperty("ErrorMessage")
        private String errorMessage;
    }

    @Data
    public static class BookingResponseDetails {
        @JsonProperty("ItineraryChangeList")
        private Object itineraryChangeList;

        @JsonProperty("PNR")
        private String pnr;

        @JsonProperty("BookingId")
        private long bookingId;

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

        @JsonProperty("TicketStatus")
        private int ticketStatus;
    }

    @Data
    public static class FlightItinerary {
        @JsonProperty("CommentDetails")
        private String commentDetails;

        @JsonProperty("FareClassification")
        private String fareClassification;

        @JsonProperty("IsAutoReissuanceAllowed")
        private boolean autoReissuanceAllowed;

        @JsonProperty("IsSeatsBooked")
        private boolean seatsBooked;

        @JsonProperty("IssuancePcc")
        private String issuancePcc;

        @JsonProperty("JourneyType")
        private int journeyType;

        @JsonProperty("SearchCombinationType")
        private int searchCombinationType;

        @JsonProperty("SupplierFareClasses")
        private Object supplierFareClasses;

        @JsonProperty("TripIndicator")
        private int tripIndicator;

        @JsonProperty("BookingAllowedForRoamer")
        private boolean bookingAllowedForRoamer;

        @JsonProperty("BookingId")
        private long bookingId;

        @JsonProperty("IsCouponApplicable")
        private boolean isCouponApplicable;

        @JsonProperty("IsManual")
        private boolean isManual;

        @JsonProperty("PNR")
        private String pnr;

        @JsonProperty("IsDomestic")
        private boolean domestic;

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

        @JsonProperty("IsLcc")
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
        private Object penaltyCharges;

        @JsonProperty("Status")
        private int status;

        @JsonProperty("Invoice")
        private List<Invoice> invoice;

        @JsonProperty("InvoiceAmount")
        private double invoiceAmount;

        @JsonProperty("InvoiceNo")
        private String invoiceNo;

        @JsonProperty("InvoiceStatus")
        private int invoiceStatus;

        @JsonProperty("InvoiceCreatedOn")
        private String invoiceCreatedOn;

        @JsonProperty("Remarks")
        private String remarks;

        @JsonProperty("IsWebCheckInAllowed")
        private boolean webCheckInAllowed;
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

        @JsonProperty("PgCharge")
        private double pgCharge;

        @JsonProperty("OtherCharges")
        private double otherCharges;

        @JsonProperty("ChargeBu")
        private List<KeyValue> chargeBu;

        @JsonProperty("Discount")
        private double discount;

        @JsonProperty("PublishedFare")
        private double publishedFare;

        @JsonProperty("CommissionEarned")
        private double commissionEarned;

        @JsonProperty("PlbEarned")
        private double plbEarned;

        @JsonProperty("IncentiveEarned")
        private double incentiveEarned;

        @JsonProperty("OfferedFare")
        private double offeredFare;

        @JsonProperty("TdsOnCommission")
        private double tdsOnCommission;

        @JsonProperty("TdsOnPlb")
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
    public static class KeyValue {
        @JsonProperty("Key")
        private String key;

        @JsonProperty("Value")
        private double value;
    }

    @Data
    public static class Passenger {
        @JsonProperty("BarcodeDetails")
        private BarcodeDetails barcodeDetails;

        @JsonProperty("DocumentDetails")
        private List<DocumentDetails> documentDetails;

        @JsonProperty("GuardianDetails")
        private Object guardianDetails;

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

        @JsonProperty("IsPanRequired")
        private boolean isPanRequired;

        @JsonProperty("IsPassportRequired")
        private boolean isPassportRequired;

        @JsonProperty("Pan")
        private String pan;

        @JsonProperty("PassportNo")
        private String passportNo;

        @JsonProperty("PassportExpiry")
        private String passportExpiry;

        @JsonProperty("AddressLine1")
        private String addressLine1;

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

        @JsonProperty("ContactNo")
        private String contactNo;

        @JsonProperty("Email")
        private String email;

        @JsonProperty("IsLeadPax")
        private boolean leadPax;

        @JsonProperty("FfAirlineCode")
        private String ffAirlineCode;

        @JsonProperty("FfNumber")
        private String ffNumber;

        @JsonProperty("Ssr")
        private List<Object> ssr;
        @JsonProperty("Ticket")
        private Ticket ticket;

        @JsonProperty("GstCompanyAddress")
        private String gstCompanyAddress;

        @JsonProperty("GstCompanyContactNumber")
        private String gstCompanyContactNumber;

        @JsonProperty("GstCompanyEmail")
        private String gstCompanyEmail;

        @JsonProperty("GstCompanyName")
        private String gstCompanyName;

        @JsonProperty("GstNumber")
        private String gstNumber;

        @JsonProperty("SegmentAdditionalInfo")
        private List<SegmentAdditionalInfo> segmentAdditionalInfo;
    }

    @Data
    public static class DocumentDetails {
        @JsonProperty("DocumentExpiryDate")
        private String documentExpiryDate;

        @JsonProperty("DocumentNumber")
        private String documentNumber;

        @JsonProperty("DocumentTypeId")
        private String documentTypeId;

        @JsonProperty("PaxId")
        private long paxId;

        @JsonProperty("ResultFareType")
        private int resultFareType;
    }

    @Data
    public static class BarcodeDetails {
        @JsonProperty("Id")
        private long id;

        @JsonProperty("Barcode")
        private List<Barcode> barcode;
    }

    @Data
    public static class Barcode {
        @JsonProperty("Index")
        private int index;

        @JsonProperty("Format")
        private String format;

        @JsonProperty("Content")
        private String content;

        @JsonProperty("BarCodeInBase64")
        private String barCodeInBase64;

        @JsonProperty("JourneyWayType")
        private int journeyWayType;
    }

    @Data
    public static class SegmentAdditionalInfo {
        @JsonProperty("FareBasis")
        private String fareBasis;

        @JsonProperty("Nva")
        private String nva;

        @JsonProperty("Nvb")
        private String nvb;

        @JsonProperty("Baggage")
        private String baggage;

        @JsonProperty("Meal")
        private String meal;

        @JsonProperty("Seat")
        private String seat;

        @JsonProperty("SpecialService")
        private String specialService;

        @JsonProperty("CabinBaggage")
        private String cabinBaggage;
    }

    @Data
    public static class Ticket {
        @JsonProperty("TicketId")
        private long ticketId;

        @JsonProperty("TicketNumber")
        private String ticketNumber;

        @JsonProperty("IssueDate")
        private String issueDate;

        @JsonProperty("ValidatingAirline")
        private String validatingAirline;

        @JsonProperty("Remarks")
        private String remarks;

        @JsonProperty("ServiceFeeDisplayType")
        private String serviceFeeDisplayType;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("ConjunctionNumber")
        private String conjunctionNumber;

        @JsonProperty("TicketType")
        private String ticketType;
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

        @JsonProperty("AirlinePnr")
        private String airlinePnr;

        @JsonProperty("Origin")
        private OriginDestination origin;

        @JsonProperty("Destination")
        private OriginDestination destination;

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

        @JsonProperty("IsEticketEligible")
        private boolean isEticketEligible;

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
    public static class OriginDestination {
        @JsonProperty("Airport")
        private Airport airport;

        @JsonProperty("DepTime")
        private String depTime;
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
    public static class Invoice {
        @JsonProperty("CreditNoteGstin")
        private String creditNoteGstin;

        @JsonProperty("Gstin")
        private String gstin;

        @JsonProperty("InvoiceCreatedOn")
        private String invoiceCreatedOn;

        @JsonProperty("InvoiceId")
        private long invoiceId;

        @JsonProperty("InvoiceNo")
        private String invoiceNo;

        @JsonProperty("InvoiceAmount")
        private double invoiceAmount;

        @JsonProperty("Remarks")
        private String remarks;

        @JsonProperty("InvoiceStatus")
        private int invoiceStatus;
    }

}

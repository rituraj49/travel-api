package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.service.TboAuthService;
import com.jamuara.crs.flight.dto.tbo.FareQuoteCacheEntry;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteRequest;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingTicketingRequest;
import com.jamuara.crs.flight.dto.tbo.book.FlightTicketRequestLcc;
import com.jamuara.crs.flight.dto.tbo.book.TBOGetBookingDetailsRequest;
import com.jamuara.crs.flight.dto.tbo.book.TravelerRequestDto;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TboFlightRequestMapper {
    private static String token = TboAuthService.getToken();

    public static Map<String, Object> mapDtoToFlightRequest(FlightSearchRequest dto) {
        boolean isReturn = StringUtils.isNotBlank(dto.getReturnDate());
        Map<String, Object> req = new HashMap<>();

        List<Map<String, Object>> segments = new ArrayList<>();

        Map<String, Object> segDep = new HashMap<>();
            segDep.put("Origin", dto.getOriginLocationCode());
            segDep.put("Destination", dto.getDestinationLocationCode());
            segDep.put("FlightCabinClass", dto.getTravelClass().ordinal() + 1);
            segDep.put("PreferredDepartureTime", dto.getDepartureDate() + "T00:00:00");

        Map<String, Object> segRet = new HashMap<>();
        if(isReturn) {
            segRet.put("Origin", dto.getDestinationLocationCode());
            segRet.put("Destination", dto.getOriginLocationCode());
            segRet.put("FlightCabinClass", dto.getTravelClass().ordinal() + 1);
            segRet.put("PreferredDepartureTime", dto.getReturnDate() + "T00:00:00");
        }

        segments.add(segDep);
        if(segRet.containsKey("Origin") && segRet.get("Origin") != null) segments.add(segRet);

        req.put("EndUserIp", "192.168.97.1");
        req.put("TokenId", token);
        req.put("AdultCount", dto.getAdults());
        req.put("ChildCount", dto.getChildren());
        req.put("InfantCount", dto.getInfants());
        req.put("DirectFlight", dto.isDirect());
        req.put("OneStopFlight", dto.isOneStop());
        req.put("JourneyType", isReturn ? 2 : 1);
        req.put("PreferredAirlines", null);
        req.put("Segments", segments);

        return req;
    }

    public static Map<String, Object> mapToFareQuoteRequest(FlightFareQuoteRequest req) {
        Map<String, Object> reqBody = new HashMap<>();

        Map<String, Object> outboundReq = new HashMap<>();
        outboundReq.put("EndUserIp", "192.168.97.1");
        outboundReq.put("TokenId", token);
        outboundReq.put("TraceId", req.getTraceId());
        outboundReq.put("ResultIndex", req.getResultIndexOutbound());

        reqBody.put("outbound", outboundReq);

        if(req.getResultIndexInbound() != null) {
            Map<String, Object> inboundReq = new HashMap<>();
            inboundReq.put("EndUserIp", "192.168.97.1");
            inboundReq.put("TokenId", token);
            inboundReq.put("TraceId", req.getTraceId());
            inboundReq.put("ResultIndex", req.getResultIndexInbound());

            reqBody.put("inbound", inboundReq);
        }

        return reqBody;
    }

    public static Map<String, Object> mapToBookingTicketingRequest(FlightBookingTicketingRequest req, Cache.ValueWrapper wrapper) {
        Map<String, Object> combinedReq = new HashMap<>();

        Map<String, Object> outboundReq = new HashMap<>();
        Map<String, Object> inboundReq = new HashMap<>();

        int totalTravelers = req.getTravelers().size();

//        Cache cache = cacheManager.getCache("fareQuote");
//        assert cache != null;
//        Cache.ValueWrapper wrapper = cache.get(req.getTraceId());

//                (FlightFareQuoteDetailsResponse) cacheManager.getCache("fareQuote").get(req.getTraceId());
        Map<String, Object> fareOutbound = new HashMap<>();
        Map<String, Object> fareInbound = new HashMap<>();
        if(wrapper != null) {
            FareQuoteCacheEntry cacheEntry = (FareQuoteCacheEntry) wrapper.get();

            if(cacheEntry != null) {
                FlightFareQuoteDetailsResponse fareDetails = (FlightFareQuoteDetailsResponse) cacheEntry.getOutboundFlight();

                fareOutbound.put("Currency", fareDetails.getCurrency());
                fareOutbound.put("BaseFare", (Double.parseDouble(fareDetails.getTotalBaseFareAmount())/totalTravelers));
                fareOutbound.put("Tax", (Double.parseDouble(fareDetails.getTotalTaxAmount())/totalTravelers));
                fareOutbound.put("YqTax", fareDetails.getYqTax());
                fareOutbound.put("pgCharge", fareDetails.getPgCharge());

                if(cacheEntry.getInboundFlight() != null) {
                    FlightFareQuoteDetailsResponse fareDetailsInbound = (FlightFareQuoteDetailsResponse) cacheEntry.getInboundFlight();

                    fareInbound.put("Currency", fareDetailsInbound.getCurrency());
                    fareInbound.put("BaseFare", (Double.parseDouble(fareDetailsInbound.getTotalBaseFareAmount())/totalTravelers));
                    fareInbound.put("Tax", (Double.parseDouble(fareDetailsInbound.getTotalTaxAmount())/totalTravelers));
                    fareInbound.put("YqTax", fareDetailsInbound.getYqTax());
                    fareInbound.put("pgCharge", fareDetailsInbound.getPgCharge());
                }
            }
        }
        List<Map<String, Object>> passengersOutbound = new ArrayList<>(req.getTravelers().size());
        List<Map<String, Object>> passengersInbound = new ArrayList<>(req.getTravelers().size());

        passengersOutbound = req.getTravelers().stream()
                .map(t -> createPassengerMap(t, fareOutbound))
                .toList();

        passengersInbound = req.getTravelers().stream()
                        .map(t -> createPassengerMap(t, fareInbound))
                        .toList();

        outboundReq.put("EndUserIp", "192.168.97.1");
        outboundReq.put("TokenId", token);
        outboundReq.put("TraceId", req.getTraceId());
        outboundReq.put("ResultIndex", req.getResultIndexOutbound());
        outboundReq.put("Passengers", passengersOutbound);

        if(!fareInbound.isEmpty()) {
            inboundReq.put("EndUserIp", "192.168.97.1");
            inboundReq.put("TokenId", token);
            inboundReq.put("TraceId", req.getTraceId());
            inboundReq.put("ResultIndex", req.getResultIndexInbound());
            inboundReq.put("Passengers", passengersInbound);

            combinedReq.put("inbound", inboundReq);
        }

        combinedReq.put("outbound", outboundReq);

        return combinedReq;
    }

    public static Map<String, Object> mapToTicketingRequest(FlightTicketRequestLcc req, CacheManager cacheManager) {
        Map<String, Object> combinedReq = new HashMap<>();

        Map<String, Object> outboundReq = new HashMap<>();
        Map<String, Object> inboundReq = new HashMap<>();

        int totalTravelers = req.getTravelers().size();

        Cache cache = cacheManager.getCache("fareQuote");
        assert cache != null;
        Cache.ValueWrapper wrapper = cache.get(req.getTraceId());

//                (FlightFareQuoteDetailsResponse) cacheManager.getCache("fareQuote").get(req.getTraceId());
        Map<String, Object> fareOutbound = new HashMap<>();
        Map<String, Object> fareInbound = new HashMap<>();
        if(wrapper != null) {
            FareQuoteCacheEntry cacheEntry = (FareQuoteCacheEntry) wrapper.get();

            if(cacheEntry != null) {
                FlightFareQuoteDetailsResponse fareDetails = (FlightFareQuoteDetailsResponse) cacheEntry.getOutboundFlight();

                fareOutbound.put("Currency", fareDetails.getCurrency());
                fareOutbound.put("BaseFare", (Double.parseDouble(fareDetails.getTotalBaseFareAmount())/totalTravelers));
                fareOutbound.put("Tax", (Double.parseDouble(fareDetails.getTotalTaxAmount())/totalTravelers));
                fareOutbound.put("YqTax", fareDetails.getYqTax());
                fareOutbound.put("pgCharge", fareDetails.getPgCharge());

                if(cacheEntry.getInboundFlight() != null) {
                    FlightFareQuoteDetailsResponse fareDetailsInbound = (FlightFareQuoteDetailsResponse) cacheEntry.getInboundFlight();

                    fareInbound.put("Currency", fareDetailsInbound.getCurrency());
                    fareInbound.put("BaseFare", (Double.parseDouble(fareDetailsInbound.getTotalBaseFareAmount())/totalTravelers));
                    fareInbound.put("Tax", (Double.parseDouble(fareDetailsInbound.getTotalTaxAmount())/totalTravelers));
                    fareInbound.put("YqTax", fareDetailsInbound.getYqTax());
                    fareInbound.put("pgCharge", fareDetailsInbound.getPgCharge());
                }
            }
        }
        List<Map<String, Object>> passengersOutbound = new ArrayList<>(req.getTravelers().size());
        List<Map<String, Object>> passengersInbound = new ArrayList<>(req.getTravelers().size());

        passengersOutbound = req.getTravelers().stream()
                .map(t -> createPassengerMap(t, fareOutbound))
                .toList();

        passengersInbound = req.getTravelers().stream()
                .map(t -> createPassengerMap(t, fareInbound))
                .toList();

        outboundReq.put("EndUserIp", "192.168.97.1");
        outboundReq.put("TokenId", token);
        outboundReq.put("TraceId", req.getTraceId());
        outboundReq.put("ResultIndex", req.getResultIndexOutbound());
        outboundReq.put("Passengers", passengersOutbound);

        if(!fareInbound.isEmpty()) {
            inboundReq.put("EndUserIp", "192.168.97.1");
            inboundReq.put("TokenId", token);
            inboundReq.put("TraceId", req.getTraceId());
            inboundReq.put("ResultIndex", req.getResultIndexInbound());
            inboundReq.put("Passengers", passengersInbound);

            combinedReq.put("inbound", inboundReq);
        }

        combinedReq.put("outbound", outboundReq);

        return combinedReq;
    }

    private static Map<String, Object> createPassengerMap(TravelerRequestDto traveler, Map<String, Object> fare) {
        Map<String, Object> pass = new HashMap<>();
        pass.put("Title", traveler.getTitle());
        pass.put("FirstName", traveler.getFirstName());
        pass.put("LastName", traveler.getLastName());
        pass.put("PaxType", traveler.getTravelerType() != null ? traveler.getTravelerType().ordinal() + 1 : null);
        pass.put("DateOfBirth", traveler.getDateOfBirth());
        pass.put("Gender", traveler.getGender() != null ? traveler.getGender().ordinal() + 1 : null);

        if (traveler.getPassportDetails() != null) {
            pass.put("PassportNo", traveler.getPassportDetails().getNumber());
            pass.put("PassportExpiry", traveler.getPassportDetails().getExpiryDate());
//                pass.put("Nationality", traveler.getPassportDetails().getNationality());
            pass.put("Nationality", traveler.getAddress().getCountryCode());
        }

        if (traveler.getAddress() != null) {
            pass.put("AddressLine1", traveler.getAddress().getLine1());
            pass.put("AddressLine2", traveler.getAddress().getLine2());
            pass.put("City", traveler.getAddress().getCity());
            pass.put("CountryCode", traveler.getAddress().getCountryCode());
        }

        pass.put("CellCountryCode", traveler.getPhoneCountryCode());
        pass.put("ContactNo", traveler.getPhone());
        pass.put("Title", traveler.getTitle());
        pass.put("FirstName", traveler.getFirstName());
        pass.put("Email", traveler.getEmail());

        pass.put("IsLeadPax", traveler.isLead());

        pass.put("Fare", fare);
        return pass;
    }




    public static Map<String, Object> mapToBookingDetailsRequest(TBOGetBookingDetailsRequest req) {


        Map<String, Object> getBookingReq = new HashMap<>();
        getBookingReq.put("EndUserIp", "192.168.97.1");
        getBookingReq.put("TokenId", token);
        getBookingReq.put("PNR", req.getPnr());
        getBookingReq.put("BookingId", req.getBookingId());

        return getBookingReq;
    }


}

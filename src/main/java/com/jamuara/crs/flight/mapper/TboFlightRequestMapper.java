package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.service.TboAuthService;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteRequest;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingRequestNonLcc;
import com.jamuara.crs.flight.dto.tbo.book.TravelerDto;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TboFlightRequestMapper {
    private static String token = TboAuthService.getToken();

    public static Map<String, Object> mapDtoToFlightRequest(FlightSearchRequest dto) {

        Map<String, Object> req = new HashMap<>();

        Map<String, Object> segDep = new HashMap<>();
            segDep.put("Origin", dto.getOriginLocationCode());
            segDep.put("Destination", dto.getDestinationLocationCode());
            segDep.put("FlightCabinClass", dto.getTravelClass().ordinal() + 1);
            segDep.put("PreferredDepartureTime", dto.getDepartureDate() + "T00:00:00");

        Map<String, Object> segRet = new HashMap<>();
        if(dto.getReturnDate() != null) {
            segRet.put("Origin", dto.getDestinationLocationCode());
            segRet.put("Destination", dto.getOriginLocationCode());
            segRet.put("FlightCabinClass", dto.getTravelClass().ordinal() + 1);
            segRet.put("PreferredDepartureTime", dto.getReturnDate() + "T00:00:00");
        }

        req.put("EndUserIp", "192.168.97.1");
        req.put("TokenId", token);
        req.put("AdultCount", dto.getAdults());
        req.put("ChildCount", dto.getChildren());
        req.put("InfantCount", dto.getInfants());
        req.put("DirectFlight", dto.isDirect());
        req.put("OneStopFlight", dto.isOneStop());
        req.put("JourneyType", dto.getReturnDate() != null ? 2 : 1);
        req.put("PreferredAirlines", null);
        req.put("Segments", List.of(segDep, segRet));

        return req;
    }

    public static Map<String, Object> mapToFareQuoteRequest(FlightFareQuoteRequest req) {
        Map<String, Object> map = new HashMap<>();
        map.put("EndUserIp", "192.168.97.1");
        map.put("TokenId", token);
        map.put("TraceId", req.getTraceId());
        map.put("ResultIndex", req.getResultIndex());

        return map;
    }

    public static Map<String, Object> mapToBookingRequest(FlightBookingRequestNonLcc req) {
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Object>> passengers = new ArrayList<>(req.getTravelers().size());

        for(int i = 0; i < req.getTravelers().size(); i++) {
            TravelerDto traveler = req.getTravelers().get(i);
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
            pass.put("Email", traveler.getEmail());

            pass.put("IsLeadPax", traveler.isLead());

            passengers.add(pass);

        }

        map.put("EndUserIp", "192.168.97.1");
        map.put("TokenId", token);
        map.put("TraceId", req.getTraceId());
        map.put("ResultIndex", req.getResultIndex());
        map.put("Passengers", passengers);

        return map;
    }


}

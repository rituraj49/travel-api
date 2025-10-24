package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.service.TboAuthService;
import com.jamuara.crs.flight.dto.tbo.FlightSearchRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TboFlightSearchRequestMapper {
    public static Map<String, Object> mapDtoToFlightRequest(FlightSearchRequest dto) {
        String token = TboAuthService.getToken();
        if(token == null) {

        }
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
}

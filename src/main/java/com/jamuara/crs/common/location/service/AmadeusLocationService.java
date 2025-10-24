package com.jamuara.crs.common.location.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.common.location.dto.LocationResponse;
import com.jamuara.crs.common.location.mapper.AmadeusLocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("amadeusLocationService")
//@Service
@Slf4j
//@Profile("dev")
public class AmadeusLocationService implements ISearchService {
    private final Amadeus amadeusClient;
    private final AmadeusLocationMapper locationMapper;

    public AmadeusLocationService(Amadeus amadeusClient, AmadeusLocationMapper locationMapper) {
        this.amadeusClient = amadeusClient;
        this.locationMapper = locationMapper;
    }

    public List<LocationResponse> keywordSearch(String keyword) {
        log.info("start search locations with keyword: {}", keyword);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("keyword", keyword);
        queryParams.put("subType", "CITY,AIRPORT");

        Params qParams = null;

        qParams = Params.with("subType", queryParams.get("subType"));
        for(Map.Entry<String, String> entry: queryParams.entrySet()) {
            if(!entry.getKey().equals("subType")) {
                qParams.and(entry.getKey(), entry.getValue());
            }
        }

        com.amadeus.resources.Location[] locations = null;
        try {
            locations = amadeusClient.referenceData.locations.get(qParams);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }

        List<com.amadeus.resources.Location> locList = new ArrayList<>(Arrays.asList(locations));

        List<Location> airports = locationMapper.toLocationList(locList);

        List<LocationResponse> locationResponseList = Helper.getGroupedLocationData(airports);
        return locationResponseList;
//        return new LocationResponseWrapper(locationResponseList);
    }

}

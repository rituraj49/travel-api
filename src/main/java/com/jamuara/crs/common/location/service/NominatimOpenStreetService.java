package com.jamuara.crs.common.location.service;

import com.jamuara.crs.common.location.dto.OSMLocationResponse;
import com.jamuara.crs.common.service.RestService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NominatimOpenStreetService {
    private RestService restService;

    public NominatimOpenStreetService(RestService restService) {
        this.restService = restService;
    }

    public List<OSMLocationResponse> keywordSearch(String keyword) {
         org.springframework.http.ResponseEntity<List<OSMLocationResponse>> res = restService.sendRequest(
                "https://nominatim.openstreetmap.org/search?format=json&q=" + keyword + "&limit=5",
                HttpMethod.GET,
                new HashMap<>(),
                null,
                new ParameterizedTypeReference<List<OSMLocationResponse>>() {}
        );

        System.out.println("response from api: " + res.toString());
         return res.getBody();
    }
}

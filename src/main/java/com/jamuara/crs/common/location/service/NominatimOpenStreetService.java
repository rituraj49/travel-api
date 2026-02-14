package com.jamuara.crs.common.location.service;

import com.jamuara.crs.common.location.dto.LocationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NominatimStreetService implements ISearchService {
    @Override
    public List<LocationResponse> keywordSearch(String keyword) {
        return List.of();
    }
}

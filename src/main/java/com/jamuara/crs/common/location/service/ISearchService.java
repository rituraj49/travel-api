package com.jamuara.crs.common.location.service;

import com.jamuara.crs.common.location.dto.LocationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISearchService {
    List<LocationResponse> keywordSearch(String keyword);
//    LocationResponseWrapper searchByGeoCode(String keyword);

}

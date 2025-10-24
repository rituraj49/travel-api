package com.jamuara.crs.activities.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import com.jamuara.crs.activities.dto.ActivityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private Amadeus amadeusClient;

    @Cacheable("activities")
    public List<ActivityResponse> getActivities(String latitude, String longitude, Integer radius) throws ResponseException {
        Map<String, List<ActivityResponse>> mockActivities = SampleActivityData.getCityAttractions();
        String key = String.valueOf(latitude) + "," + String.valueOf(longitude);
        if(mockActivities.containsKey(key)) {
            log.info("returning from mock data");
            return mockActivities.get(key);
        }
        Params params = Params.with("latitude", latitude)
                .and("longitude", longitude);

        if (radius != null) {
            params.and("radius", radius.toString());
            params.and("radiusUnit", "KM");
        }

        Activity[] activities = amadeusClient.shopping.activities.get(params);

        List<ActivityResponse> activityResponses = Arrays.stream(activities).map(activity -> {
            ActivityResponse dto = new ActivityResponse();
            dto.setId(activity.getId());
            dto.setType(activity.getType());
            dto.setName(activity.getName());
            dto.setDescription(activity.getDescription());
            dto.setShortDescription(activity.getShortDescription());
            dto.setRating(activity.getRating());

            if (activity.getGeoCode() != null) {
                dto.setGeoCode(new ActivityResponse.GeoCode(
                        String.valueOf(activity.getGeoCode().getLatitude()),
                        String.valueOf(activity.getGeoCode().getLongitude())
                ));
            }

             dto.setRating(activity.getRating());

            if (activity.getPictures() != null) {
                dto.setPictures(Arrays.asList(activity.getPictures()));
            }

            dto.setBookingLink(activity.getBookingLink());

            if (activity.getPrice() != null) {
                dto.setPrice(new ActivityResponse.Price(
                        activity.getPrice().getCurrencyCode(),
                        activity.getPrice().getAmount()
                ));
            }
            dto.setMinimumDuration(activity.getMinimumDuration());

            return dto;
        }).toList();

//        activityResponses.subList(1, 5);
        return activityResponses;
    }

    @Cacheable("activitiesSquare")
    public List<ActivityResponse> getActivitiesBySquare(
            String north,
            String west,
            String south,
            String east
    ) throws ResponseException {

        Params params = Params.with("north", north)
                .and("west", west)
                .and("south", south)
                .and("east", east);

        Activity[] activities = amadeusClient.shopping.activities.bySquare.get(params);
//        Arrays.asList(activities);
        return Arrays.stream(activities).map(activity -> {
            ActivityResponse dto = new ActivityResponse();
            dto.setId(activity.getId());
            dto.setType(activity.getType());
            dto.setName(activity.getName());
            dto.setDescription(activity.getDescription());
            dto.setShortDescription(activity.getShortDescription());

            dto.setRating(activity.getRating());
            if (activity.getGeoCode() != null) {
                dto.setGeoCode(new ActivityResponse.GeoCode(
                        String.valueOf(activity.getGeoCode().getLatitude()),
                        String.valueOf(activity.getGeoCode().getLongitude())
                ));
            }

            if (activity.getPictures() != null) {
                dto.setPictures(Arrays.asList(activity.getPictures()));
            }

            dto.setBookingLink(activity.getBookingLink());

            if (activity.getPrice() != null) {
                dto.setPrice(new ActivityResponse.Price(
                        activity.getPrice().getCurrencyCode(),
                        activity.getPrice().getAmount()
                ));
            }

            dto.setMinimumDuration(activity.getMinimumDuration());

            return dto;
        }).toList();
    }
}

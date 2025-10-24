package com.jamuara.crs.activities.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Activity;
import com.jamuara.crs.activities.dto.ActivityResponse;
import com.jamuara.crs.activities.service.ActivityService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("activities")
@Slf4j
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<?> getActivities(
            @Parameter(description = " latitude (decimal coordinates)",
                    example = "41.397158")
            @RequestParam String latitude,
            @Parameter(description = " longitude (decimal coordinates)",
                    example = "2.160873")
            @RequestParam String longitude,
            @RequestParam(required = false, defaultValue = "1") Integer radius
    ) {
        try {
            List<ActivityResponse> activities = activityService.getActivities(latitude, longitude, radius);
            return ResponseEntity.ok(activities);
        } catch (ResponseException e) {
            log.error("Error fetching activities: {}", e.getDescription());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getDescription());
        }
    }

    @GetMapping("/square")
    public ResponseEntity<?> getActivitiesBySquare(
            @Parameter(description = " north latitude (decimal coordinates)",
                    example = "41.397158")
            @RequestParam String north,
            @Parameter(description = " south latitude (decimal coordinates)",
                    example = "41.387158")
            @RequestParam String south,
            @Parameter(description = " east longitude (decimal coordinates)",
                    example = "2.160873")
            @RequestParam String east,
            @Parameter(description = " west longitude (decimal coordinates)",
                    example = "2.150873")
            @RequestParam String west
    ) {
        try {
            List<ActivityResponse> activities = activityService.getActivitiesBySquare(north, south, east, west);
            return ResponseEntity.ok(activities);
        } catch (ResponseException e) {
            log.error("Error fetching activities: {}", e.getDescription());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getDescription());
        }
    }
}
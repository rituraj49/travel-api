package com.jamuara.crs.travel_package.controller;

import com.jamuara.crs.travel_package.dto.TravelPackageRequestDto;
import com.jamuara.crs.travel_package.dto.TravelPackageResponseDto;
import com.jamuara.crs.travel_package.service.TravelPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("package")
public class TravelPackageController {
    @Autowired
    private TravelPackageService travelPackageService;

    @PostMapping("book")
    public ResponseEntity<?> bookTravelPackage(@RequestBody TravelPackageRequestDto travelPackageRequestDto) {
        try {
            TravelPackageResponseDto response = travelPackageService.travelPackageBooking(travelPackageRequestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong: " + e.getMessage());
        }
    }
}

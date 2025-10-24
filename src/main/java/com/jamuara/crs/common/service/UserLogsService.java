package com.jamuara.crs.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.flight.dto.FlightBookingRequest;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.common.repository.UsersLogsRepository;
import com.jamuara.crs.model.UserLogs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@Profile("!nodb")
public class UserLogsService {

    @Autowired
    private UsersLogsRepository usersLogsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void saveUserLog(String orderId, LocalDateTime logTimestamp, String requestPayload, String responsePayload,
                            Integer numberOfTravellers, String totalAmount,
                            String fromLocation, String toLocation, String currencyCode) {
        UserLogs log1 = new UserLogs(orderId, logTimestamp, requestPayload, responsePayload,
                numberOfTravellers, totalAmount, fromLocation, toLocation, currencyCode);

//        System.out.println(log1);

        usersLogsRepository.save(log1);
    }

    public void createUserLogs(FlightBookingRequest orderRequest, FlightBookingResponse createdOrder) {
        try {
            String requestJson = objectMapper.writeValueAsString(orderRequest);
            String responseJson = objectMapper.writeValueAsString(createdOrder);

            Integer numberOfTravellers = orderRequest.getTravelers() != null ? orderRequest.getTravelers().size() : 0;

            String totalAmount = createdOrder.getFlightOffer().getTotalPrice();
            String from = createdOrder.getFlightOffer().getTrips().get(0).getFrom();
            String to = createdOrder.getFlightOffer().getTrips().get(0).getTo();
            String currencyCode = createdOrder.getFlightOffer().getCurrencyCode();


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String name=authentication.getDetails().toString();

            if (authentication.getPrincipal() instanceof Jwt jwt) {
                username = jwt.getClaimAsString("preferred_username");
            }

            log.info(" Flight booking initiated by user : {} ", username );

            // Save log
            saveUserLog(
                    createdOrder.getOrderId(),
                    LocalDateTime.now(),
                    requestJson+username,
                    responseJson,
                    numberOfTravellers,
                    totalAmount,
                    from,
                    to,
                    currencyCode
            );

        } catch (JsonProcessingException e) {
            log.error("Error converting request/response to JSON: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
package com.jamuara.crs.admin.priceChanges;

import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteResponse;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchMulticityRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DynamicPricingService {

    private final PriceRuleRepository priceRuleRepository;

    public DynamicPricingService(PriceRuleRepository priceRuleRepository) {
        this.priceRuleRepository = priceRuleRepository;
    }

    // MAIN PRICE LOGIC
    public double applyDynamicPrice(double basePrice,
                                    PriceRule.TripType tripType) {

        List<PriceRule> rules =
                priceRuleRepository.findActiveRules(
                        tripType, LocalDate.now()
                );

        double finalPrice = basePrice;

        for (PriceRule rule : rules) {
            double change = (finalPrice * rule.getPercentage()) / 100;
            finalPrice += change;
        }

        return Math.round(finalPrice * 100.0) / 100.0;
    }

    //  APPLY ON SEARCH

    public void applyMarkupOnSearch(FlightSearchResponse response,
                                    FlightSearchRequest request) {

        System.out.println(" APPLY MARKUP METHOD CALLED");

        if (response.getFlightsAvailable() == null) return;

        PriceRule.TripType tripType =
                getTripType(
                        request.getOriginLocationCode(),
                        request.getDestinationLocationCode()
                );

        response.getFlightsAvailable().forEach((key, flightList) -> {

            flightList.forEach(flight -> {


                double baseFare = Double.parseDouble(
                        flight.getPublishedFare()//.replaceAll("[^0-9.]", "")
                );

                System.out.println(baseFare +"   "+tripType);
                double finalFare =
                        applyDynamicPrice(baseFare, tripType);


                System.out.println(
                        "BEFORE: " + baseFare + " | AFTER: " + finalFare
                );

                flight.setPublishedFare(String.valueOf(finalFare));
            });
        });
    }





    //  DOMESTIC / INTERNATIONAL DETECTION
    public PriceRule.TripType getTripType(String origin,
                                           String destination) {

        boolean isDomestic =
                origin.substring(0, 2)
                        .equalsIgnoreCase(destination.substring(0, 2));

        return isDomestic
                ? PriceRule.TripType.DOMESTIC
                : PriceRule.TripType.INTERNATIONAL;
    }



    public void applyMarkupByResultIndex(
            FlightFareQuoteResponse response,
            String resultIndex) {

        if (response.getFlightsAvailable() == null || resultIndex == null) return;

        response.getFlightsAvailable().forEach((key, flightList) -> {

            flightList.forEach(flight -> {

                //  Apply markup ONLY on matching ResultIndex
                if (resultIndex.equals(flight.getResultIndex())) {

                    double baseFare = Double.parseDouble(
                            flight.getPublishedFare()//.replaceAll("[^0-9.]", "")
                    );

                    //  Example: Flat 10% markup based on index
                    double finalFare = baseFare + (baseFare * 10 / 100);

                    flight.setPublishedFare(String.valueOf(finalFare));
                }
            });
        });
    }




    public void applyMarkupOnMulticitySearch(
            FlightSearchResponse response,
            FlightSearchMulticityRequest request) {

        if (response.getFlightsAvailable() == null) return;
        if (request.getTripDetails() == null || request.getTripDetails().isEmpty()) return;

        //  Take FIRST LEG only
        FlightSearchMulticityRequest.TripDetailsDto firstTrip =
                request.getTripDetails().get(0);

        PriceRule.TripType tripType = getTripType(
                firstTrip.getOriginLocationCode(),
                firstTrip.getDestinationLocationCode()
        );

        //  FETCH ACTIVE MARKUP RULES
        List<PriceRule> rules =
                priceRuleRepository.findActiveRules(tripType, LocalDate.now());

        //  NO MARKUP FOUND → RETURN SAME RESPONSE
        if (rules == null || rules.isEmpty()) {
            System.out.println("NO MULTICITY MARKUP FOUND FOR: " + tripType);
            return;
        }

        System.out.println("APPLY MULTICITY MARKUP CALLED");

        response.getFlightsAvailable().forEach((key, flightList) -> {
            flightList.forEach(flight -> {

                if (flight.getPublishedFare() == null) return;

                double baseFare = Double.parseDouble(
                        flight.getPublishedFare()//.replaceAll("[^0-9.]", "")
                );

                double finalFare = baseFare;

                //  APPLY ALL ACTIVE RULES
                for (PriceRule rule : rules) {
                    double change = (finalFare * rule.getPercentage()) / 100;
                    finalFare += change;
                }

                finalFare = Math.round(finalFare * 100.0) / 100.0;
                flight.setPublishedFare(String.valueOf(finalFare));

                System.out.println(
                        "BASE FARE : " + baseFare + "   FINAL FARE : " + finalFare
                );
            });
        });
    }











    public void applyMarkupOnFetchBooking(FetchFlightBookingResponse response) {

        if (response == null ||
                response.getTicketBookingDetails() == null ||
                response.getTicketBookingDetails().getFlightDetails() == null ||
                response.getTicketBookingDetails().getFlightDetails().getTicketFare() == null) {
            return;
        }

        FetchFlightBookingResponse.TicketBookFlightDetails flightDetails =
                response.getTicketBookingDetails().getFlightDetails();

        //  CONVERT ENUM TYPE SAFELY
        com.jamuara.crs.enums.TripType apiTripType = flightDetails.getTripType();

        PriceRule.TripType tripType =
                PriceRule.TripType.valueOf(apiTripType.name());  //  FIX

        //  FETCH ACTIVE MARKUP RULE
        List<PriceRule> rules =
                priceRuleRepository.findActiveRules(tripType, LocalDate.now());

        if (rules == null || rules.isEmpty()) {
            System.out.println("NO BOOKING MARKUP FOUND FOR: " + tripType);
            return;
        }

        FetchFlightBookingResponse.TicketFare fare =
                flightDetails.getTicketFare();

        if (fare.getPublishedFare() == null) return;

        double baseFare = Double.parseDouble(
                fare.getPublishedFare()//.replaceAll("[^0-9.]", "")
        );

        double finalFare = baseFare;

        for (PriceRule rule : rules) {
            double change = (finalFare * rule.getPercentage()) / 100;
            finalFare += change;
        }

        finalFare = Math.round(finalFare * 100.0) / 100.0;

        fare.setPublishedFare(String.valueOf(finalFare));

        System.out.println(
                "BOOKING MARKUP APPLIED | BASE: " + baseFare + " | FINAL: " + finalFare
        );
    }





}

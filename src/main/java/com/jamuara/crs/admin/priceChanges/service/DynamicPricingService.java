package com.jamuara.crs.admin.priceChanges.service;

import com.jamuara.crs.admin.priceChanges.model.PriceRule;
import com.jamuara.crs.admin.priceChanges.repository.PriceRuleRepository;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteResponse;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchMulticityRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchRequest;
import com.jamuara.crs.flight.dto.tbo.search.FlightSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class DynamicPricingService {

    private static final Logger log = LoggerFactory.getLogger(DynamicPricingService.class);
    private final PriceRuleRepository priceRuleRepository;

    public DynamicPricingService(PriceRuleRepository priceRuleRepository) {
        this.priceRuleRepository = priceRuleRepository;
    }

    // MAIN PRICE LOGIC
    public double applyDynamicPrice(double basePrice, List<PriceRule> rules) {

//        List<PriceRule> rules =
//                priceRuleRepository.findActiveRules(LocalDate.now());

        double finalPrice = basePrice;

        for (PriceRule rule : rules) {
            double change = (finalPrice * rule.getPercentage()) / 100;
            finalPrice += change;
        }

        return Math.round(finalPrice * 100.0) / 100.0;
    }

    //  APPLY ON SEARCH
    public void applyMarkupOnSearch(FlightSearchResponse response) {

        if (response.getFlightsAvailable() == null) return;

        List<PriceRule> activeRules = priceRuleRepository.findActiveRules(LocalDate.now());
        response.getFlightsAvailable().forEach((key, flightList) -> {

            flightList.forEach(flight -> {

                double baseFare = Double.parseDouble(
                        flight.getPublishedFare()
                );

                double finalFare = applyDynamicPrice(baseFare, activeRules);

                flight.setPublishedFare(String.valueOf(finalFare));
                log.info("Base Fare : {}, Final Fare : {}", baseFare,finalFare);

            });
        });
    }

    public void applyMarkupByResultIndex(
            FlightFareQuoteResponse response,
            String resultIndex) {

        if (response.getFlightsAvailable() == null || resultIndex == null) return;

        List<PriceRule> activeRules = priceRuleRepository.findActiveRules(LocalDate.now());
        response.getFlightsAvailable().forEach((key, flightList) -> {

            flightList.forEach(flight -> {

                //  Apply markup ONLY on matching ResultIndex
                if (resultIndex.equals(flight.getResultIndex())) {

                    double baseFare = Double.parseDouble(
                            flight.getPublishedFare()//.replaceAll("[^0-9.]", "")
                    );

                    //  Example: Flat 10% markup based on index
//                    double finalFare = baseFare + (baseFare * 10 / 100);
                    double finalFare = applyDynamicPrice(baseFare, activeRules);

                    flight.setPublishedFare(String.valueOf(finalFare));
                }
            });
        });
    }

    public void applyMarkupOnMulticitySearch(
            FlightSearchResponse response,
            FlightSearchMulticityRequest request) {

        if (response.getFlightsAvailable() == null) return;

        List<PriceRule> rules =
                priceRuleRepository.findActiveRules(LocalDate.now());

        if (rules == null || rules.isEmpty()) return;

        response.getFlightsAvailable().forEach((key, flightList) -> {
            flightList.forEach(flight -> {

                if (flight.getPublishedFare() == null) return;

                double baseFare = Double.parseDouble(flight.getPublishedFare());
                double finalFare = applyDynamicPrice(baseFare, rules);
//
//                for (PriceRule rule : rules) {
//                    double change = (finalFare * rule.getPercentage()) / 100;
//                    finalFare += change;
//                }
//
//                finalFare = Math.round(finalFare * 100.0) / 100.0;
                flight.setPublishedFare(String.valueOf(finalFare));

                log.info("Multi City | Base Fare : {}, Final Fare : {}", baseFare,finalFare);

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

        List<PriceRule> rules =
                priceRuleRepository.findActiveRules(LocalDate.now());

        if (rules == null || rules.isEmpty()) {
            log.info("NO BOOKING MARKUP FOUND");
            return;
        }

        FetchFlightBookingResponse.TicketFare fare =
                flightDetails.getTicketFare();

        if (fare.getPublishedFare() == null) return;

        double baseFare = Double.parseDouble(fare.getPublishedFare());
        double finalFare = applyDynamicPrice(baseFare, rules);

//        for (PriceRule rule : rules) {
//            double change = (finalFare * rule.getPercentage()) / 100;
//            finalFare += change;
//        }

//        finalFare = Math.round(finalFare * 100.0) / 100.0;
        fare.setPublishedFare(String.valueOf(finalFare));
        log.info("BOOKING MARKUP APPLIED | Base Fare : {}, Final Fare : {}", baseFare,finalFare);
    }
}
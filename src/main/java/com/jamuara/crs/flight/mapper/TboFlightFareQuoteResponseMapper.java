package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.enums.TripType;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.FlightFareQuoteResponse;
import com.jamuara.crs.flight.dto.tbo.TboApiFareQuoteResponseDto;
import com.jamuara.crs.flight.dto.tbo.search.TboApiFlightResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(config = CentralMapperConfig.class, imports = {LocalDateTime.class, Duration.class})
public interface TboFlightFareQuoteResponseMapper {

    @Mapping(source = "traceId", target = "traceId")
    @Mapping(source = "flightDetailChangeInfo", target = "flightDetailChangeInfo")
    @Mapping(source = "priceChanged", target = "priceChanged")
    @Mapping(source = "itineraryChangeList",target = "itineraryChangeList")
    @Mapping(expression = "java(mapFlights(tboDto.getResults()))", target = "flightsAvailable")
    FlightFareQuoteResponse mapToFlightFareQuoteResponse(TboApiFareQuoteResponseDto.Response tboDto);

    @Mapping(target = "legNo", source = "segmentIndicator")
    @Mapping(target = "tripType", expression = "java(com.jamuara.crs.enums.TripType.values()[segment.getTripIndicator() - 1])")
    @Mapping(target = "baggage", source = "baggage")
    @Mapping(target = "cabinBaggage", source = "cabinBaggage")
    @Mapping(target = "cabinClass", expression = "java(com.jamuara.crs.enums.TravelClass.values()[segment.getCabinClass() - 1])")
    @Mapping(target = "carrierCode", source = "airline.airlineCode")
    @Mapping(target = "carrierName", source = "airline.airlineName")
    @Mapping(target = "flightNumber", source = "airline.flightNumber")
    @Mapping(target = "operatingCarrier", source = "airline.operatingCarrier")
    @Mapping(target = "aircraftCode", source = "craft")
    @Mapping(target = "departureAirport", source = "origin.airport.airportCode")
    @Mapping(target = "departureAirportName", source = "origin.airport.airportName")
    @Mapping(target = "departureTerminal", source = "origin.airport.terminal")
    @Mapping(target = "departureCityName", source = "origin.airport.cityName")
    @Mapping(target = "departureCountryName", source = "origin.airport.countryName")
    @Mapping(target = "departureDateTime", source = "origin.depTime")
    @Mapping(target = "arrivalAirport", source = "destination.airport.airportCode")
    @Mapping(target = "arrivalAirportName", source = "destination.airport.airportName")
    @Mapping(target = "arrivalTerminal", source = "destination.airport.terminal")
    @Mapping(target = "arrivalCityName", source = "destination.airport.cityName")
    @Mapping(target = "arrivalCountryName", source = "destination.airport.countryName")
    @Mapping(target = "arrivalDateTime", source = "destination.arrTime")
    @Mapping(target = "duration", expression = "java(formatDuration(segment.getDuration()))")
    @Mapping(target = "layoverDuration", ignore = true)
    @Mapping(target = "fareBasisCode", ignore = true)
    FlightFareQuoteDetailsResponse.FlightLeg mapFlightLegs(TboApiFlightResponseDto.Response.Segment segment);

    default Map<String, List<FlightFareQuoteDetailsResponse>> mapFlights(TboApiFareQuoteResponseDto.Results results) {
        if (results == null) return Map.of();

        FlightFareQuoteDetailsResponse flight = new FlightFareQuoteDetailsResponse();
        flight.setResultIndex(results.getResultIndex());
        flight.setLCC(results.isLCC());
        flight.setRefundable(results.isRefundable());
        flight.setValidatingAirline(results.getValidatingAirline());
        flight.setAirlineRemark(results.getAirlineRemark());
        flight.setLastTicketDate(results.getLastTicketDate());
        flight.setFareType(results.getFareClassification() != null ? results.getFareClassification().getType() : null);
        flight.setFareColor(results.getFareClassification() != null ? results.getFareClassification().getColor() : null);


        if (results.getFare() != null) {
            var fare = results.getFare();
            flight.setCurrency(fare.getCurrency());
            flight.setTotalBaseFareAmount(String.valueOf(fare.getBaseFare()));
            flight.setTotalTaxAmount(String.valueOf(fare.getTax()));
            flight.setYqTax(String.valueOf(fare.getYqTax()));
            flight.setPgCharge(String.valueOf(fare.getPgCharge()));
            flight.setOtherCharges(String.valueOf(fare.getOtherCharges()));
            flight.setPublishedFare(String.valueOf(fare.getPublishedFare()));
            flight.setOfferedFare(String.valueOf(fare.getOfferedFare()));
            flight.setCommissionEarned(String.valueOf(fare.getCommissionEarned()));
            flight.setIncentiveEarned(String.valueOf(fare.getIncentiveEarned()));
            flight.setPlbEarned(String.valueOf(fare.getPlbEarned()));
            flight.setTotalBaggageCharges(String.valueOf(fare.getTotalBaggageCharges()));
            flight.setTotalMealCharges(String.valueOf(fare.getTotalMealCharges()));
            flight.setTotalSeatCharges(String.valueOf(fare.getTotalSeatCharges()));

            if (fare.getTaxBreakup() != null) {
                flight.setTaxBreakup(
                        fare.getTaxBreakup().stream().map(t -> {
                            var tb = new FlightFareQuoteDetailsResponse.TaxChargeBreakup();
                                tb.setKey(t.getKey());
                                tb.setValue(String.valueOf(t.getValue()));
                            return tb;
                        }).collect(Collectors.toList())
                );
            }

            // chargesBreakup
            if (fare.getChargeBU() != null) {
                flight.setChargesBreakup(
                        fare.getChargeBU().stream().map(c -> {
                            var cb = new FlightFareQuoteDetailsResponse.TaxChargeBreakup();
                            cb.setKey(c.getKey());
                            cb.setValue(String.valueOf(c.getValue()));
                            return cb;
                        }).collect(Collectors.toList())
                );
            }
        }

        if (results.getFareBreakdown() != null) {
            flight.setTravelerDetails(
                    results.getFareBreakdown().stream().map(b -> {
                        var fd = new FlightFareQuoteDetailsResponse.FareDetails();
                        switch (b.getPassengerType()) {
                            case 1 -> fd.setTravelerType(FlightFareQuoteDetailsResponse.TravelerType.ADULT);
                            case 2 -> fd.setTravelerType(FlightFareQuoteDetailsResponse.TravelerType.CHILD);
                            case 3 -> fd.setTravelerType(FlightFareQuoteDetailsResponse.TravelerType.INFANT);
                        }
                        fd.setTravelersCount(b.getPassengerCount());
                        fd.setBaseFare(String.valueOf(b.getBaseFare()));
                        fd.setTax(String.valueOf(b.getTax()));
                        fd.setYqTax(String.valueOf(b.getYqTax()));
                        fd.setPgCharge(String.valueOf(b.getPgCharge()));

                        if (b.getTaxBreakUp() != null) {
                            fd.setTaxBreakup(
                                    b.getTaxBreakUp().stream().map(t -> {
                                        var tb = new FlightFareQuoteDetailsResponse.TaxChargeBreakup();
                                        tb.setKey(t.getKey());
                                        tb.setValue(String.valueOf(t.getValue()));
                                        return tb;
                                    }).collect(Collectors.toList())
                            );
                        }
                        return fd;
                    }).collect(Collectors.toList())
            );
        }

        if (results.getSegments() != null) {
            flight.setFlightLegs(
                    results.getSegments().stream()
                            .flatMap(List::stream)
                            .map(segment -> {
                                var leg = new FlightFareQuoteDetailsResponse.FlightLeg();
//                                leg.setTripType(com.jamuara.crs.enums.TripType.RETURN);
                                leg.setTripType(TripType.values()[segment.getTripIndicator() - 1]);
                                leg.setBaggage(segment.getBaggage());
                                leg.setCabinBaggage(segment.getCabinBaggage());
                                leg.setCabinClass(com.jamuara.crs.enums.TravelClass.values()[segment.getCabinClass() - 1]);
                                leg.setCarrierCode(segment.getAirline().getAirlineCode());
                                leg.setCarrierName(segment.getAirline().getAirlineName());
                                leg.setOperatingCarrier(segment.getAirline().getOperatingCarrier());
                                leg.setFlightNumber(segment.getAirline().getFlightNumber());
                                leg.setAircraftCode(segment.getCraft());
                                leg.setDepartureAirport(segment.getOrigin().getAirport().getAirportCode());
                                leg.setDepartureAirportName(segment.getOrigin().getAirport().getAirportName());
                                leg.setDepartureTerminal(segment.getOrigin().getAirport().getTerminal());
                                leg.setDepartureCityName(segment.getOrigin().getAirport().getCityName());
                                leg.setDepartureCountryName(segment.getOrigin().getAirport().getCountryName());
                                leg.setDepartureDateTime(segment.getOrigin().getDepTime());
                                leg.setArrivalAirport(segment.getDestination().getAirport().getAirportCode());
                                leg.setArrivalAirportName(segment.getDestination().getAirport().getAirportName());
                                leg.setArrivalTerminal(segment.getDestination().getAirport().getTerminal());
                                leg.setArrivalCityName(segment.getDestination().getAirport().getCityName());
                                leg.setArrivalCountryName(segment.getDestination().getAirport().getCountryName());
                                leg.setArrivalDateTime(segment.getDestination().getArrTime());
                                leg.setDuration(formatDuration(segment.getDuration()));
                                return leg;
                            })
                    .collect(Collectors.toList())
            );
            List<FlightFareQuoteDetailsResponse.FlightLeg> allLegs = new ArrayList<>();
            Duration totalLayover = Duration.ZERO;

            for (List<TboApiFareQuoteResponseDto.Segment> segmentList : results.getSegments()) {
                for (int i = 0; i < segmentList.size(); i++) {
                    var segment = segmentList.get(i);
                    var leg = new FlightFareQuoteDetailsResponse.FlightLeg();

                    leg.setTripType(TripType.values()[segment.getTripIndicator() - 1]);
                    leg.setBaggage(segment.getBaggage());
                    leg.setCabinBaggage(segment.getCabinBaggage());
                    leg.setCabinClass(com.jamuara.crs.enums.TravelClass.values()[segment.getCabinClass() - 1]);
                    leg.setCarrierCode(segment.getAirline().getAirlineCode());
                    leg.setCarrierName(segment.getAirline().getAirlineName());
                    leg.setOperatingCarrier(segment.getAirline().getOperatingCarrier());
                    leg.setFlightNumber(segment.getAirline().getFlightNumber());
                    leg.setAircraftCode(segment.getCraft());
                    leg.setDepartureAirport(segment.getOrigin().getAirport().getAirportCode());
                    leg.setDepartureAirportName(segment.getOrigin().getAirport().getAirportName());
                    leg.setDepartureTerminal(segment.getOrigin().getAirport().getTerminal());
                    leg.setDepartureCityName(segment.getOrigin().getAirport().getCityName());
                    leg.setDepartureCountryName(segment.getOrigin().getAirport().getCountryName());
                    leg.setDepartureDateTime(segment.getOrigin().getDepTime());
                    leg.setArrivalAirport(segment.getDestination().getAirport().getAirportCode());
                    leg.setArrivalAirportName(segment.getDestination().getAirport().getAirportName());
                    leg.setArrivalTerminal(segment.getDestination().getAirport().getTerminal());
                    leg.setArrivalCityName(segment.getDestination().getAirport().getCityName());
                    leg.setArrivalCountryName(segment.getDestination().getAirport().getCountryName());
                    leg.setArrivalDateTime(segment.getDestination().getArrTime());
                    leg.setDuration(formatDuration(segment.getDuration()));

                    if (i < segmentList.size() - 1) {
                        LocalDateTime arr = LocalDateTime.parse(segment.getDestination().getArrTime());
                        LocalDateTime nextDep = LocalDateTime.parse(segmentList.get(i + 1).getOrigin().getDepTime());
                        Duration layover = Duration.between(arr, nextDep);
                        leg.setLayoverDuration(Helper.getDurationString(layover.toString()));
                        totalLayover = totalLayover.plus(layover);
                    }

                    allLegs.add(leg);
                }
            }

            flight.setFlightLegs(allLegs);
            flight.setTotalLayover(Helper.getDurationString(totalLayover.toString()));
        } else {
            flight.setFlightLegs(List.of());
        }

        return Map.of("flight", List.of(flight));
    }

    default String formatDuration(int durationInMinutes) {
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    FlightFareQuoteDetailsResponse toFlightFareQuoteDetailsResponse(TboApiFareQuoteResponseDto.Results result);

    @AfterMapping
    default void calculateLayovers(TboApiFareQuoteResponseDto.Results result, @MappingTarget FlightFareQuoteDetailsResponse flightDetailsResponse) {
        Duration totalLayover = Duration.ZERO;
        var fareRules = result.getFareRules();

        int globalIndex = 0;

        for(List<TboApiFareQuoteResponseDto.Segment> segmentList: result.getSegments()) {
            for(int i = 0; i < segmentList.size(); i++) {
                TboApiFareQuoteResponseDto.Segment segment = segmentList.get(i);
                FlightFareQuoteDetailsResponse.FlightLeg currentLeg = flightDetailsResponse.getFlightLegs().get(globalIndex);

                if(fareRules != null) {
                    String fareBasisCode = fareRules.get(globalIndex).getFareBasisCode();
                    currentLeg.setFareBasisCode(fareBasisCode);
                }

                if(i < segmentList.size() - 1) {
                    LocalDateTime arr = LocalDateTime.parse(segment.getDestination().getArrTime());
                    LocalDateTime nextDep = LocalDateTime.parse(segmentList.get(i + 1).getOrigin().getDepTime());
                    Duration layover = Duration.between(arr, nextDep);
                    currentLeg.setLayoverDuration(Helper.getDurationString(layover.toString()));
                    totalLayover = totalLayover.plus(layover);
                } else {
                    currentLeg.setLayoverDuration(null);
                }

                globalIndex++;
            }
        }

        flightDetailsResponse.setTotalLayover(Helper.getDurationString(totalLayover.toString()));

    }
}

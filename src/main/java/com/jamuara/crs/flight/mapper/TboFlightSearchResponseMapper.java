package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.enums.TripType;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.FlightSearchResponse;
import com.jamuara.crs.flight.dto.tbo.TboApiFlightResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface TboFlightSearchResponseMapper {
    @Mapping(source = "traceId", target = "traceId")
    @Mapping(source = "origin", target = "from")
    @Mapping(source = "destination", target = "to")
    @Mapping(expression = "java(mapFlights(tboDto.getResults()))", target = "flightsAvailable")
    FlightSearchResponse mapToFlightSearchResponse(TboApiFlightResponseDto.Response tboDto);

    default Map<String, List<FlightDetailsResponse>> mapFlights(List<List<TboApiFlightResponseDto.Response.Result>> results) {
        if(results == null) return null;

        Map<String, List<FlightDetailsResponse>> map = new LinkedHashMap<>();

        for(int i = 0; i < results.size(); i++) {
            List<TboApiFlightResponseDto.Response.Result> flightResults = results.get(i);
            String key = "";

            if(results.size() == 1) {
                key = "outboundFlights";
            } else if(results.size() == 2) {
                key = i == 0 ? "outboundFlights" : "inboundFlights";
            } else if(results.size() > 2) {
                key = "trip " + i + 1;
            }

            List<FlightDetailsResponse> flightDetailsResponses = flightResults.stream()
                    .map(this::mapResultsToFlightDetailsResponse)
                    .toList();

            map.put(key, flightDetailsResponses);
        }
        return map;
    }

    @Mapping(source = "resultIndex", target = "resultIndex")
    @Mapping(source = "refundable", target = "refundable")
    @Mapping(source = "LCC", target = "LCC")
    @Mapping(source = "passportRequiredAtTicket", target = "passportRequired")
    @Mapping(source = "fare.currency", target = "currency")
    @Mapping(source = "lastTicketDate", target = "lastTicketDate")
    @Mapping(source = "fare.baseFare", target = "totalBaseFareAmount")
    @Mapping(source = "fare.tax", target = "totalTaxAmount")
    @Mapping(source = "fare.taxBreakup", target = "taxBreakup")
    @Mapping(source = "fare.yqTax", target = "yqTax")
    @Mapping(source = "fare.pgCharge", target = "pgCharge")
    @Mapping(source = "fare.otherCharges", target = "otherCharges")
    @Mapping(source = "fare.chargeBU", target = "chargesBreakup")
    @Mapping(source = "fare.publishedFare", target = "publishedFare")
//    @Mapping(expression = "java(result.getSegments().stream().flatMap(List::stream).map(this::mapFlightLeg).toList())", target = "flightLegs")
    @Mapping(expression = "java(mapFlattenedSegments(result.getSegments()))", target = "flightLegs")
    @Mapping(source = "fareBreakdown", target = "travelerDetails")
    FlightDetailsResponse mapResultsToFlightDetailsResponse(TboApiFlightResponseDto.Response.Result result);

    default List<FlightDetailsResponse.FlightLeg> mapFlattenedSegments(List<List<TboApiFlightResponseDto.Response.Segment>> segments) {
        return segments.stream()
                .flatMap(List::stream)
                .map(this::mapFlightLegs)
                .toList();
    }

    @Mapping(target = "travelerType", expression = "java(FlightDetailsResponse.TravelerType.values()[fareBreakdown.getPassengerType() - 1])")
    @Mapping(target = "travelersCount", source = "passengerCount")
    @Mapping(target = "baseFare", source = "baseFare")
    @Mapping(target = "tax", source = "tax")
    @Mapping(target = "taxBreakup", source = "taxBreakUp")
    @Mapping(source = "yqTax", target = "yqTax")
    @Mapping(source = "pgCharge", target = "pgCharge")
    FlightDetailsResponse.FareDetails mapFareDetails(TboApiFlightResponseDto.Response.FareBreakdown fareBreakdown);

    @Mapping(target = "legNo", source = "segmentIndicator")
    @Mapping(target = "tripType", expression = "java(com.jamuara.crs.enums.TripType.values()[segment.getTripIndicator() - 1])")
    @Mapping(target = "seatsAvailable", source = "noOfSeatAvailable")
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
    FlightDetailsResponse.FlightLeg mapFlightLegs(TboApiFlightResponseDto.Response.Segment segment);

    default String formatDuration(int durationInMinutes) {
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    @AfterMapping
    default void calculateLayovers(TboApiFlightResponseDto.Response.Result result, @MappingTarget FlightDetailsResponse flightDetailsResponse) {
        Duration totalLayover = Duration.ZERO;
        var fareRules = result.getFareRules();

        int globalIndex = 0;

        for(List<TboApiFlightResponseDto.Response.Segment> segmentList: result.getSegments()) {
            for(int i = 0; i < segmentList.size(); i++) {
                TboApiFlightResponseDto.Response.Segment segment = segmentList.get(i);
                FlightDetailsResponse.FlightLeg currentLeg = flightDetailsResponse.getFlightLegs().get(globalIndex);
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

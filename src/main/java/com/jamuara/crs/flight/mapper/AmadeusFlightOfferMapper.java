package com.jamuara.crs.flight.mapper;

import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.JsonObject;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.enums.TripType;
import com.jamuara.crs.flight.dto.FlightAvailabilityResponse;
import org.mapstruct.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface AmadeusFlightOfferMapper {
    @Mapping(source = "oneWay", target = "oneWay")
    @Mapping(source = "numberOfBookableSeats", target = "seatsAvailable")
    @Mapping(source = "price.currency", target = "currencyCode")
    @Mapping(source = "price.base", target = "basePrice")
    @Mapping(source = "price.grandTotal", target = "totalPrice")
    @Mapping(expression = "java(offer.getTravelerPricings().length)", target = "totalTravelers")
    @Mapping(source = "itineraries", target = "trips")
    @Mapping(expression = "java(new com.google.gson.Gson().toJson(offer))", target = "pricingAdditionalInfo")
    FlightAvailabilityResponse toFlightAvailabilityResponse(FlightOfferSearch offer, @Context JsonObject dictionaries);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "type", target = "type")

    FlightAvailabilityResponse.Fees toFees(FlightOfferSearch.Fee fee);

//    @Mapping(source = "segments", target = "legs")
    @Mapping(target = "tripNo", ignore = true)
    @Mapping(target = "tripType", ignore = true)
    @Mapping(source = "segments", target = "legs")
    @Mapping(target = "from", ignore = true)
    @Mapping(target = "to", ignore = true)
    @Mapping(target = "stops", ignore = true)
    @Mapping(target = "totalFlightDuration", ignore = true)
    @Mapping(target = "totalLayoverDuration", ignore = true)
    FlightAvailabilityResponse.Trip toTrip(FlightOfferSearch.Itinerary itinerary, @Context JsonObject dictionaries);

    @Mapping(source = "id", target = "legNo")
    @Mapping(source = "number", target = "flightNumber")
    @Mapping(source = "operating.carrierCode", target = "operatingCarrierCode")
    @Mapping(target = "operatingCarrierName", ignore = true)
    @Mapping(source = "carrierCode", target = "carrierCode")
    @Mapping(target = "carrierName", ignore = true)
    @Mapping(source = "aircraft.code", target = "aircraftCode")
    @Mapping(target = "aircraft", ignore = true)
    @Mapping(source = "departure.iataCode", target = "departureAirport")
    @Mapping(source = "departure.terminal", target = "departureTerminal")
    @Mapping(source = "segment.departure.at", target = "departureDateTime")
    @Mapping(source = "arrival.iataCode", target = "arrivalAirport")
    @Mapping(source = "arrival.terminal", target = "arrivalTerminal")
    @Mapping(source = "segment.arrival.at", target = "arrivalDateTime")
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "layoverAfter", ignore = true)
    FlightAvailabilityResponse.Leg toLeg(FlightOfferSearch.SearchSegment segment, @Context JsonObject dictionaries);

    @AfterMapping
    default void mapTrips(FlightOfferSearch.Itinerary itinerary, @Context JsonObject dictionaries, @MappingTarget FlightAvailabilityResponse.Trip trip) {
        FlightOfferSearch.SearchSegment[] segments = itinerary.getSegments();

        String from = segments[0].getDeparture().getIataCode();
        String to = segments[segments.length - 1].getArrival().getIataCode();
        int stops = segments.length - 1;
        trip.setFrom(from);
        trip.setTo(to);
        trip.setStops(stops);

        Duration totalLayover = Duration.ZERO;
        Duration totalDuration = Duration.ZERO;

        for (int j = 0; j < segments.length; j++) {
            totalDuration = totalDuration.plus(Duration.parse(segments[j].getDuration()));
            if(trip.getLegs() != null && trip.getLegs().size() == segments.length) {
                if(dictionaries != null && !dictionaries.isEmpty()) {
                    JsonObject carriers = dictionaries.getAsJsonObject("carriers");

                    String operatingCarrierName = "AIRLINE";
                    if (segments[j].getOperating() != null) {
                        String code = String.valueOf(segments[j].getOperating().getCarrierCode());
                        if (carriers.has(code)) {
                            operatingCarrierName = carriers.get(code).getAsString();
                        }
                    }

                    String carrierName = "AIRLINE";
                    if (segments[j].getCarrierCode() != null) {
                        String code = String.valueOf(segments[j].getCarrierCode());
                        if (carriers.has(code)) {
                            carrierName = carriers.get(code).getAsString();
                        }
                    }

                    JsonObject aircrafts = dictionaries.getAsJsonObject("aircraft");

                    String aircraftName = "AIRCRAFT";
                    if (segments[j].getAircraft() != null) {
                        String code = String.valueOf(segments[j].getAircraft().getCode());
                        if (aircrafts.has(code)) {
                            aircraftName = aircrafts.get(code).getAsString();
                        }
                    }
//                }
//                    if(trip.getLegs() != null && trip.getLegs().size() == segments.length) {

                    trip.getLegs().get(j).setOperatingCarrierName(operatingCarrierName);
                    trip.getLegs().get(j).setCarrierName(carrierName);
                    trip.getLegs().get(j).setAircraft(aircraftName);
                }
                        trip.getLegs().get(j).setDuration(Helper.getDurationString(segments[j].getDuration()));
                        if (j < segments.length - 1) {
                            LocalDateTime arrival = LocalDateTime.parse(segments[j].getArrival().getAt());
                            LocalDateTime nextDeparture = LocalDateTime.parse(segments[j + 1].getDeparture().getAt());
                            Duration layover = Duration.between(arrival, nextDeparture);
                            totalLayover = totalLayover.plus(layover);
                            totalDuration = totalDuration.plus(layover);
                            trip.getLegs().get(j).setLayoverAfter(Helper.getDurationString(String.valueOf(layover)));
                        }
                }
//                    }
        }
//        totalDuration = totalLayover.plus(totalLayover);
        trip.setTotalFlightDuration(Helper.getDurationString(String.valueOf(totalDuration)));
        trip.setTotalLayoverDuration(Helper.getDurationString(String.valueOf(totalLayover)));
    }

    @AfterMapping
    default void finalizeResponse(FlightOfferSearch offer, @MappingTarget FlightAvailabilityResponse resp) {
        List<FlightAvailabilityResponse.Trip> trips = resp.getTrips();
        if (trips == null) return;

        for (int i = 0; i < trips.size(); i++) trips.get(i).setTripNo(i + 1);

        int n = trips.size();
        if (n == 1) trips.forEach(t -> t.setTripType(TripType.OUTBOUND));
        else if (n == 2) trips.forEach(t -> t.setTripType(TripType.RETURN));
        else trips.forEach(t -> t.setTripType(TripType.MULTICITY));

/*

        //  Apply 10% price up
        if (resp.getBasePrice() != null) {
            try {
                BigDecimal base = new BigDecimal(resp.getBasePrice());
                resp.setBasePrice(base.multiply(BigDecimal.valueOf(1.1))
                        .setScale(2, RoundingMode.HALF_UP)
                        .toString());
            } catch (NumberFormatException ignored) {}
        }

        if (resp.getTotalPrice() != null) {
            try {
                BigDecimal total = new BigDecimal(resp.getTotalPrice());
                resp.setTotalPrice(total.multiply(BigDecimal.valueOf(1.1))
                        .setScale(2, RoundingMode.HALF_UP)
                        .toString());
            } catch (NumberFormatException ignored) {}
        }
*/

    }

    default FlightAvailabilityResponse mapFirstOffer(FlightOfferSearch[] offers) {
        if (offers == null || offers.length == 0) return null;
        return toFlightAvailabilityResponse(offers[0], new JsonObject());
    }
}

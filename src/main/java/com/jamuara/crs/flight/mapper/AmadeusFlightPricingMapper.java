package com.jamuara.crs.flight.mapper;

import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.enums.TripType;
import com.jamuara.crs.flight.dto.FlightPricingResponse;
import org.mapstruct.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(config = CentralMapperConfig.class,  imports = { LocalDateTime.class })
public interface AmadeusFlightPricingMapper {
    @Mapping(source = "oneWay", target = "oneWay")
    @Mapping(source = "numberOfBookableSeats", target = "seatsAvailable")
    @Mapping(source = "price.currency", target = "currencyCode")
    @Mapping(source = "price.base", target = "basePrice")
    @Mapping(source = "price.grandTotal", target = "totalPrice")
//    @Mapping(source = "price.base", target = "basePrice", qualifiedByName = "priceWithMarkup")
//    @Mapping(source = "price.grandTotal", target = "totalPrice", qualifiedByName = "priceWithMarkup")
    @Mapping(expression = "java(offer.getTravelerPricings().length)", target = "totalTravelers")
    @Mapping(source = "price.fees", target = "fees")
    @Mapping(source = "itineraries", target = "trips")
    @Mapping(expression = "java(new com.google.gson.Gson().toJson(offer))", target = "bookingAdditionalInfo")
    @Mapping(expression = "java(mapCreditCardFees(creditCardFees))", target = "creditCardFees")
    FlightPricingResponse toFlightPricingResponse(FlightOfferSearch offer, @Context JsonObject creditCardFees);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "type", target = "type")
    FlightPricingResponse.Fees toFees(FlightOfferSearch.Fee fee);

/*
    @Named("priceWithMarkup")
    default String priceWithMarkup(String price) {
        if (price == null || price.isEmpty()) return "0.00";
        double original = Double.parseDouble(price);
        double withMarkup = original * 1.10; // add 10%
        return String.format("%.2f", withMarkup); // keep 2 decimal places
    }
*/



    //    @Mapping(source = "segments", target = "legs")
    @Mapping(target = "tripNo", ignore = true)
    @Mapping(target = "tripType", ignore = true)
    @Mapping(source = "segments", target = "legs")
    @Mapping(target = "from", ignore = true)
    @Mapping(target = "to", ignore = true)
    @Mapping(target = "stops", ignore = true)
    @Mapping(target = "totalFlightDuration", ignore = true)
    @Mapping(target = "totalLayoverDuration", ignore = true)
    FlightPricingResponse.Trip toTrip(FlightOfferSearch.Itinerary itinerary, @Context JsonObject dictionaries);

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
    FlightPricingResponse.Leg toLeg(FlightOfferSearch.SearchSegment segment, @Context JsonObject dictionaries);

    @AfterMapping
    default void mapTrips(FlightOfferSearch.Itinerary itinerary, @Context JsonObject dictionaries, @MappingTarget FlightPricingResponse.Trip trip) {
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
            if (j < segments.length - 1) {
                LocalDateTime arrival = LocalDateTime.parse(segments[j].getArrival().getAt());
                LocalDateTime nextDeparture = LocalDateTime.parse(segments[j + 1].getDeparture().getAt());
                Duration layover = Duration.between(arrival, nextDeparture);
                totalLayover = totalLayover.plus(layover);
                totalDuration = totalDuration.plus(layover);

                JsonObject carriers  = dictionaries.getAsJsonObject("carriers");
                String operatingCarrierName = "AIRLINE";
                String carrierName = "AIRLINE";
                String aircraftName = "AIRCRAFT";

                if(carriers != null && !carriers.isEmpty()) {

                    if (segments[j].getOperating() != null) {
                        String code = String.valueOf(segments[j].getOperating().getCarrierCode());
                        if (carriers.has(code)) {
                            operatingCarrierName = carriers.get(code).getAsString();
                        }
                    }

                    if(segments[j].getCarrierCode() != null) {
                        String code = String.valueOf(segments[j].getCarrierCode());
                        if (carriers.has(code)) {
                            carrierName = carriers.get(code).getAsString();
                        }
                    }

                    JsonObject aircrafts = dictionaries.getAsJsonObject(("aircraft"));

                    if(segments[j].getAircraft() != null) {
                        String code = String.valueOf(segments[j].getAircraft().getCode());
                        if (aircrafts.has(code)) {
                            aircraftName = aircrafts.get(code).getAsString();
                        }
                    }

                }
                trip.getLegs().get(j).setOperatingCarrierName(operatingCarrierName);
                trip.getLegs().get(j).setCarrierName(carrierName);
                trip.getLegs().get(j).setAircraft(aircraftName);
                trip.getLegs().get(j).setDuration(Helper.getDurationString(segments[j].getDuration()));
                trip.getLegs().get(j).setLayoverAfter(Helper.getDurationString(layover.toString()));
            }
        }
        trip.setTotalFlightDuration(Helper.getDurationString(totalDuration.toString()));
        trip.setTotalLayoverDuration(Helper.getDurationString(totalLayover.toString()));
    }

    @AfterMapping
    default void finalizeResponse(FlightOfferSearch offer, @MappingTarget FlightPricingResponse resp) {
        List<FlightPricingResponse.Trip> trips = resp.getTrips();
        if (trips == null) return;

        for (int i = 0; i < trips.size(); i++) trips.get(i).setTripNo(i + 1);

        int n = trips.size();
        if (n == 1) trips.forEach(t -> {
            t.setTripType(TripType.OUTBOUND);
        });
        else if (n == 2) trips.forEach(t -> t.setTripType(TripType.RETURN));
        else trips.forEach(t -> t.setTripType(TripType.MULTICITY));
    }

//    @Named("durationMapper")
//    default String getDurationString(String duration) {
//        if(duration == null || duration.isEmpty()) {
//            return "0h 0m";
//        }
//        Duration dur = Duration.parse(duration);
//        long hrs = dur.toHours();
//        long min = dur.minusHours(hrs).toMinutes();
//        return String.format("%dh %dm", hrs, min);
//    }

    default List<FlightPricingResponse.CreditCardFees> mapCreditCardFees(JsonObject creditCardFeesJson) {
        List<FlightPricingResponse.CreditCardFees> fees = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : creditCardFeesJson.entrySet()) {
            JsonObject obj = entry.getValue().getAsJsonObject();
            FlightPricingResponse.CreditCardFees fee = new FlightPricingResponse.CreditCardFees();
            fee.setBrand(obj.get("brand").getAsString());
            fee.setAmount(obj.get("amount").getAsString());
            fee.setCurrency(obj.get("currency").getAsString());
            fees.add(fee);
        }
        return fees;
    }
}

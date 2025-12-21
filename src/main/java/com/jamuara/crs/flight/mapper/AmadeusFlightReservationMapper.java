package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.FlightAvailabilityResponse;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.flight.dto.TravelerRequestDto;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.model.FlightLeg;
import com.jamuara.crs.model.Reservation;
import com.jamuara.crs.model.Traveler;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface AmadeusFlightReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingResponse", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "travelers", ignore = true)
    @Mapping(target = "flightLegs", ignore = true)
    @Mapping(target = "bookingId", source = "response.orderId")
    @Mapping(target = "pnr", ignore = true)
    @Mapping(target = "domestic", ignore = true)
    @Mapping(target = "price", source = "flightDetails.totalPrice")
    @Mapping(target = "currencyCode", source = "flightDetails.currencyCode")
    @Mapping(target = "lastTicketDate", ignore = true)
    @Mapping(target = "origin", source = "trip.from")
    @Mapping(target = "destination", source = "trip.to")
    public Reservation toReservation(FlightBookingResponse response, FlightAvailabilityResponse flightDetails, FlightAvailabilityResponse.Trip trip);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "phone", qualifiedByName = "firstPhoneNumber", source = ".")
    @Mapping(target = "phoneCountryCode", qualifiedByName = "firstPhoneCode", source = ".")
    @Mapping(target = "travelerType", ignore = true)
    public Traveler toTraveler(TravelerRequestDto travelerDto);

    @Named("firstPhoneNumber")
    default String firstPhoneNumber(TravelerRequestDto dto) {
        return dto.getPhones() != null && !dto.getPhones().isEmpty()
                ? dto.getPhones().get(0).getNumber()
                : null;
    }

    @Named("firstPhoneCode")
    default String firstPhoneCode(TravelerRequestDto dto) {
        return dto.getPhones() != null && !dto.getPhones().isEmpty()
                ? dto.getPhones().get(0).getCountryCallingCode()
                : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "legNo", source = "leg.legNo")
    @Mapping(target = "tripNo", source = "trip.tripNo")
    @Mapping(target = "carrierCode", source = "leg.carrierCode")
    @Mapping(target = "carrierName", source = "leg.carrierName")
    @Mapping(target = "operatingCarrier", source = "leg.operatingCarrierName")
    @Mapping(target = "flightNumber", source = "leg.flightNumber")
    @Mapping(target = "aircraftCode", source = "leg.aircraftCode")
    @Mapping(target = "departureAirport", source = "leg.departureAirport")
    @Mapping(target = "arrivalAirport", source = "leg.arrivalAirport")
    @Mapping(target = "duration", source = "trip.totalFlightDuration")
    @Mapping(target = "layoverDuration", source = "trip.totalLayoverDuration")
//    @Mapping(target = "departureDateTime", expression = "java(formatDate(flightLegDto.getDepartureDateTime()))")
    @Mapping(target = "departureDateTime", qualifiedByName = "formatDateTime", source = "leg.departureDateTime")
//    @Mapping(target = "arrivalDateTime", expression = "java(formatDate(flightLegDto.getArrivalDateTime()))")
    @Mapping(target = "arrivalDateTime", qualifiedByName = "formatDateTime", source = "leg.arrivalDateTime")
    public FlightLeg toFlightLeg(FlightAvailabilityResponse.Leg leg, FlightAvailabilityResponse.Trip trip);

    @Named("formatDateTime")
    default String formatDate(String dateStr) {
        if(dateStr == null) return "";
        return LocalDateTime
                .parse(dateStr)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }

    @AfterMapping
    default void linkChildren(@MappingTarget Reservation reservation, FlightBookingResponse source) {
        for(Traveler trv: reservation.getTravelers()) {
            trv.setReservation(reservation);
        }

        for(FlightLeg leg: reservation.getFlightLegs()) {
            leg.setReservation(reservation);
        }
    }
}

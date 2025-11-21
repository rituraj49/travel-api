package com.jamuara.crs.common.service;

import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.model.FlightLeg;
import com.jamuara.crs.model.Reservation;
import com.jamuara.crs.model.Traveler;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingResponse", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "travelers", source = "ticketBookingDetails.flightDetails.travelers")
    @Mapping(target = "flightLegs", source = "ticketBookingDetails.flightDetails.flightLegs")
    @Mapping(target = "bookingId", source = "ticketBookingDetails.bookingId")
    @Mapping(target = "pnr", source = "ticketBookingDetails.pnr")
    @Mapping(target = "lcc", source = "ticketBookingDetails.flightDetails.LCC")
    @Mapping(target = "domestic", source = "ticketBookingDetails.flightDetails.domestic")
    @Mapping(target = "price", source = "ticketBookingDetails.flightDetails.ticketFare.publishedFare")
    @Mapping(target = "currencyCode", source = "ticketBookingDetails.flightDetails.ticketFare.currency")
    @Mapping(target = "lastTicketDate", source = "ticketBookingDetails.flightDetails.lastTicketDate")
    @Mapping(target = "origin", source = "ticketBookingDetails.flightDetails.origin")
    @Mapping(target = "destination", source = "ticketBookingDetails.flightDetails.destination")
    public Reservation toReservation(FetchFlightBookingResponse response);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "phoneCountryCode", source = "phoneCountryCode")
    @Mapping(target = "travelerType", source = "travelerType")
    public Traveler toTraveler(FetchFlightBookingResponse.TicketTravelerDto travelerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "legNo", source = "legNo")
    @Mapping(target = "tripNo", source = "tripNo")
    @Mapping(target = "carrierCode", source = "carrierCode")
    @Mapping(target = "carrierName", source = "carrierName")
    @Mapping(target = "operatingCarrier", source = "operatingCarrier")
    @Mapping(target = "flightNumber", source = "flightNumber")
    @Mapping(target = "aircraftCode", source = "aircraftCode")
    @Mapping(target = "departureAirport", source = "departureAirport")
    @Mapping(target = "arrivalAirport", source = "arrivalAirport")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "layoverDuration", source = "layoverDuration")
//    @Mapping(target = "departureDateTime", expression = "java(formatDate(flightLegDto.getDepartureDateTime()))")
    @Mapping(target = "departureDateTime", qualifiedByName = "formatDateTime")
//    @Mapping(target = "arrivalDateTime", expression = "java(formatDate(flightLegDto.getArrivalDateTime()))")
    @Mapping(target = "arrivalDateTime", qualifiedByName = "formatDateTime")
    public FlightLeg toFlightLeg(FlightDetailsResponse.FlightLeg flightLegDto);

    @Named("formatDateTime")
    default String formatDate(String dateStr) {
        if(dateStr == null) return "";
        return LocalDateTime
                .parse(dateStr)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }

    @AfterMapping
    default void linkChildren(@MappingTarget Reservation reservation, FetchFlightBookingResponse source) {
        for(Traveler trv: reservation.getTravelers()) {
            trv.setReservation(reservation);
        }

        for(FlightLeg leg: reservation.getFlightLegs()) {
            leg.setReservation(reservation);
        }
    }
}

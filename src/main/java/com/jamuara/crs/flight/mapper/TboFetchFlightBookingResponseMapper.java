package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.TboApiFetchFlightBookingResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface TboFetchFlightBookingResponseMapper {

    @Mapping(source = "response.traceId", target = "traceId")
    @Mapping(source = "response.flightItinerary", target = "ticketBookingDetails")
    FetchFlightBookingResponse toFetchFlightBookingResponse(TboApiFetchFlightBookingResponseDto source);

    @Mapping(source = "pnr", target = "pnr")
    @Mapping(source = "bookingId", target = "bookingId")
    @Mapping(expression = "java(com.jamuara.crs.enums.TicketStatus.values()[source.getStatus()])", target = "ticketStatus")
    @Mapping(source = ".", target = "flightDetails")
    FetchFlightBookingResponse.TicketBookingDetails toTicketBookingDetails(TboApiFetchFlightBookingResponseDto.Response.FlightItinerary source);

//    @Mapping(source = "pnr", target = "pnr")
//    @Mapping(source = "bookingId", target = "bookingId")
//    @Mapping(expression = "java(com.jamuara.crs.enums.TicketStatus.values()[source.getStatus()])", target = "ticketStatus")
    @Mapping(source = "LCC", target = "LCC")
//    @Mapping(source = "issuancePcc", target = "issuancePcc")
    @Mapping(source = "nonRefundable", target = "nonRefundable")
    @Mapping(expression = "java(com.jamuara.crs.enums.TripType.values()[source.getTripIndicator() - 1])", target = "tripType")
    @Mapping(source = "domestic", target = "domestic")
    @Mapping(source = "validatingAirlineCode", target = "validatingAirline")
    @Mapping(source = "origin", target = "origin")
    @Mapping(source = "destination", target = "destination")
    @Mapping(source = "lastTicketDate", target = "lastTicketDate")
    @Mapping(source = "airlineTollFreeNo", target = "airlineTollFreeNo")
    @Mapping(source = "fare", target = "ticketFare")
    @Mapping(source = "passenger", target = "travelers")
    @Mapping(source = "segments", target = "flightLegs")
    @Mapping(source = "webCheckInAllowed", target = "webCheckInEligible")
    FetchFlightBookingResponse.TicketBookFlightDetails toTicketBookFlightDetails(TboApiFetchFlightBookingResponseDto.Response.FlightItinerary source);

    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "baseFare", target = "totalBaseFareAmount")
    @Mapping(source = "tax", target = "totalTaxAmount")
    @Mapping(source = "taxBreakup", target = "taxBreakup")
    @Mapping(source = "yqTax", target = "yqTax")
    @Mapping(source = "pgCharge", target = "pgCharge")
    @Mapping(source = "otherCharges", target = "otherCharges")
    @Mapping(source = "chargeBu", target = "chargesBreakup")
    @Mapping(source = "publishedFare", target = "publishedFare")
    @Mapping(source = "serviceFee", target = "serviceFee")
    @Mapping(source = "totalBaggageCharges", target = "baggageCharges")
    @Mapping(source = "totalMealCharges", target = "mealCharges")
    @Mapping(source = "totalSeatCharges", target = "seatCharges")
    @Mapping(source = "totalSpecialServiceCharges", target = "specialServiceCharges")
    FetchFlightBookingResponse.TicketFare toTicketFare(TboApiFetchFlightBookingResponseDto.Response.Fare source);

    @Mapping(source = "paxId", target = "travelerId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "email", target = "email")
    @Mapping(expression = "java(com.jamuara.crs.enums.Gender.values()[source.getGender() - 1])", target = "gender")
    @Mapping(expression = "java(com.jamuara.crs.enums.TravelerType.values()[source.getPaxType() - 1])", target = "travelerType")
    @Mapping(source = "contactNo", target = "phone")
    @Mapping(source = "leadPax", target = "lead")
    @Mapping(expression = "java(source.getDocumentDetails() != null && !source.getDocumentDetails().isEmpty() ? toDocumentDetails(source.getDocumentDetails().get(0)) : null)", target = "documentDetails")
    @Mapping(source = "fare", target = "farePerTraveler")
    @Mapping(source = "barcodeDetails", target = "barcodeDetails")
//    @Mapping(expression = "java(source.getSegmentAdditionalInfo() != null && !source.getSegmentAdditionalInfo().isEmpty() ? toAdditionalInfo(source.getSegmentAdditionalInfo().get(0)) : null)", target = "additionalInfo")
//    @Mapping(source = "ticket", target = "ticket")
    FetchFlightBookingResponse.TicketTravelerDto toTicketTraveler(TboApiFetchFlightBookingResponseDto.Response.Passenger source);

    @Mapping(source = "documentNumber", target = "number")
    @Mapping(source = "documentExpiryDate", target = "expiryDate")
    @Mapping(source = "documentTypeId", target = "documentType")
    @Mapping(expression = "java(String.valueOf(source.getPaxId()))", target = "travelerId")
    FetchFlightBookingResponse.DocumentDetails toDocumentDetails(TboApiFetchFlightBookingResponseDto.Response.Passenger.DocumentDetail source);

//    // === Barcode ===
//    @Mapping(expression = "java(source.getBarcode().get(0).getFormat())", target = "format")
//    @Mapping(expression = "java(source.getBarcode().get(0).getContent())", target = "content")
//    @Mapping(expression = "java(source.getBarcode().get(0).getBarCodeInBase64())", target = "inBase64")
//    FetchFlightBookingResponse.BarcodeDetails toBarcodeDetails(TboApiFetchFlightBookingResponseDto.Response.BarcodeDetails source);

    @Mapping(target = "legNo", source = "segmentIndicator")
    @Mapping(target = "tripNo", source = "tripIndicator")
    @Mapping(source = "airline.airlineCode", target = "carrierCode")
    @Mapping(source = "airline.airlineName", target = "carrierName")
    @Mapping(source = "airline.operatingCarrier", target = "operatingCarrier")
    @Mapping(source = "airline.flightNumber", target = "flightNumber")
    @Mapping(source = "craft", target = "aircraftCode")
    @Mapping(expression = "java(com.jamuara.crs.enums.TravelClass.values()[source.getCabinClass() - 1])", target = "cabinClass")
    @Mapping(source = "origin.airport.airportCode", target = "departureAirport")
    @Mapping(source = "origin.airport.airportName", target = "departureAirportName")
    @Mapping(source = "origin.airport.terminal", target = "departureTerminal")
    @Mapping(source = "origin.airport.cityName", target = "departureCityName")
    @Mapping(source = "origin.airport.countryName", target = "departureCountryName")
    @Mapping(source = "origin.depTime", target = "departureDateTime")
    @Mapping(source = "destination.airport.airportCode", target = "arrivalAirport")
    @Mapping(source = "destination.airport.airportName", target = "arrivalAirportName")
    @Mapping(source = "destination.airport.terminal", target = "arrivalTerminal")
    @Mapping(source = "destination.airport.cityName", target = "arrivalCityName")
    @Mapping(source = "destination.airport.countryName", target = "arrivalCountryName")
    @Mapping(source = "destination.arrTime", target = "arrivalDateTime")
    @Mapping(target = "duration", expression = "java(formatDuration(source.getDuration()))")
    @Mapping(source = "fareClassification", target = "fareBasisCode")
    @Mapping(source = "airlinePnr", target = "airlinePnr")
    FlightDetailsResponse.FlightLeg toFlightLeg(TboApiFetchFlightBookingResponseDto.Response.Segment source);

    default String formatDuration(int durationInMinutes) {
        int hours = durationInMinutes / 60;
        int minutes = durationInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    FlightDetailsResponse.TaxChargeBreakup toTaxChargeBreakup(TboApiFetchFlightBookingResponseDto.Response.Fare.KeyValue source);

    @AfterMapping
    default void calculateLayovers(TboApiFetchFlightBookingResponseDto.Response.FlightItinerary source,
                                   @MappingTarget FetchFlightBookingResponse.TicketBookFlightDetails target) {
        if (source == null || source.getSegments() == null || source.getSegments().isEmpty()) return;

        Duration totalDuration = Duration.ZERO;
        Duration totalLayover = Duration.ZERO;
        List<FlightDetailsResponse.FlightLeg> legs = target.getFlightLegs();
        List<TboApiFetchFlightBookingResponseDto.Response.Segment> segments = source.getSegments();

        for (int i = 0; i < segments.size(); i++) {
            TboApiFetchFlightBookingResponseDto.Response.Segment segment = segments.get(i);
            FlightDetailsResponse.FlightLeg leg = legs.get(i);

            totalDuration = totalDuration.plus(Duration.ofMinutes(segment.getDuration()));
            if (i < segments.size() - 1) {
                String arrTime = segment.getDestination().getArrTime();
                String nextDepTime = segments.get(i + 1).getOrigin().getDepTime();

                try {
                    LocalDateTime arrival = LocalDateTime.parse(arrTime);
                    LocalDateTime nextDeparture = LocalDateTime.parse(nextDepTime);
                    Duration layover = Duration.between(arrival, nextDeparture);
                    leg.setLayoverDuration(Helper.getDurationString(layover.toString()));
                    totalLayover = totalLayover.plus(layover);
                } catch (Exception ignored) {
                    leg.setLayoverDuration(null);
                }
            } else {
                leg.setLayoverDuration(null);
            }
            totalDuration = totalDuration.plus(totalLayover);
        }

        target.setTotalLayover(Helper.getDurationString(totalLayover.toString()));
        target.setTotalDuration(Helper.getDurationString(totalDuration.toString()));
    }
}
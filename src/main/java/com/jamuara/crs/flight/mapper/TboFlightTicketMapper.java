package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.tbo.FlightDetailsResponse;
import com.jamuara.crs.flight.dto.tbo.book.FlightTicketResponse;
import com.jamuara.crs.flight.dto.tbo.book.TboApiFlightTicketResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface TboFlightTicketMapper {
    // === Root Mapping ===
    @Mapping(source = "response.traceId", target = "traceId")
    @Mapping(source = "response", target = "ticketBookingDetails")
    FlightTicketResponse toFlightTicketResponse(TboApiFlightTicketResponseDto source);

    // === TicketBookingDetails ===
    @Mapping(source = "pnr", target = "pnr")
    @Mapping(source = "bookingId", target = "bookingId")
    @Mapping(expression = "java(com.jamuara.crs.flight.dto.tbo.book.FlightTicketResponse.TicketStatus.values()[source.getTicketStatus()])", target = "ticketStatus")
    @Mapping(source = "priceChanged", target = "priceChanged")
    @Mapping(source = "timeChanged", target = "timeChanged")
    @Mapping(source = "flightItinerary", target = "flightDetails")
    FlightTicketResponse.TicketBookingDetails toTicketBookingDetails(TboApiFlightTicketResponseDto.BookingResponseDetails source);

    // === TicketBookFlightDetails ===
    @Mapping(source = "LCC", target = "LCC")
    @Mapping(source = "issuancePcc", target = "issuancePcc")
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
    @Mapping(source = "invoice", target = "invoice")
    @Mapping(source = "webCheckInAllowed", target = "webCheckInEligible")
    FlightTicketResponse.TicketBookFlightDetails toTicketBookFlightDetails(TboApiFlightTicketResponseDto.FlightItinerary source);

    // === TicketFare ===
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
    FlightTicketResponse.TicketFare toTicketFare(TboApiFlightTicketResponseDto.Fare source);

    // === Traveler ===
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
    @Mapping(expression = "java(source.getSegmentAdditionalInfo() != null && !source.getSegmentAdditionalInfo().isEmpty() ? toAdditionalInfo(source.getSegmentAdditionalInfo().get(0)) : null)", target = "additionalInfo")
    @Mapping(source = "ticket", target = "ticket")
    FlightTicketResponse.TicketTravelerDto toTicketTraveler(TboApiFlightTicketResponseDto.Passenger source);

    // === Document ===
    @Mapping(source = "documentNumber", target = "number")
    @Mapping(source = "documentExpiryDate", target = "expiryDate")
    @Mapping(source = "documentTypeId", target = "documentType")
    @Mapping(expression = "java(String.valueOf(source.getPaxId()))", target = "travelerId")
    FlightTicketResponse.DocumentDetails toDocumentDetails(TboApiFlightTicketResponseDto.DocumentDetails source);

    // === Barcode ===
    @Mapping(expression = "java(source.getBarcode().get(0).getFormat())", target = "format")
    @Mapping(expression = "java(source.getBarcode().get(0).getContent())", target = "content")
    @Mapping(expression = "java(source.getBarcode().get(0).getBarCodeInBase64())", target = "inBase64")
    FlightTicketResponse.BarcodeDetails toBarcodeDetails(TboApiFlightTicketResponseDto.BarcodeDetails source);


    // === Additional Info ===
    @Mapping(source = "fareBasis", target = "fareBasisCode")
    @Mapping(source = "nva", target = "notValidAfterDate")
    @Mapping(source = "nvb", target = "notValidBeforeDate")
    @Mapping(source = "baggage", target = "baggage")
    @Mapping(source = "cabinBaggage", target = "cabinBaggage")
    @Mapping(source = "meal", target = "meal")
    @Mapping(source = "specialService", target = "specialService")
    FlightTicketResponse.AdditionalInfo toAdditionalInfo(TboApiFlightTicketResponseDto.SegmentAdditionalInfo source);

    // === Ticket ===
    @Mapping(source = "ticketId", target = "ticketId")
    @Mapping(source = "ticketNumber", target = "ticketNumber")
    @Mapping(source = "issueDate", target = "issueDate")
    @Mapping(source = "validatingAirline", target = "validatingAirline")
    FlightTicketResponse.Ticket toTicket(TboApiFlightTicketResponseDto.Ticket source);

    // === FlightLeg ===
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
    @Mapping(source = "destination.depTime", target = "arrivalDateTime")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "fareClassification", target = "fareBasisCode")
    @Mapping(source = "airlinePnr", target = "airlinePnr")
    FlightDetailsResponse.FlightLeg toFlightLeg(TboApiFlightTicketResponseDto.Segment source);

    // === Invoice ===
    @Mapping(source = "invoiceCreatedOn", target = "invoiceDate")
    @Mapping(source = "invoiceId", target = "invoiceId")
    @Mapping(source = "invoiceNo", target = "invoiceNo")
    @Mapping(source = "invoiceAmount", target = "invoiceAmount")
    FlightTicketResponse.Invoice toInvoice(TboApiFlightTicketResponseDto.Invoice source);

    // === TaxBreakup ===
    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    FlightDetailsResponse.TaxChargeBreakup toTaxChargeBreakup(TboApiFlightTicketResponseDto.KeyValue source);

    // === AfterMapping for Layover ===
    @AfterMapping
    default void calculateLayovers(TboApiFlightTicketResponseDto.FlightItinerary source,
                                   @MappingTarget FlightTicketResponse.TicketBookFlightDetails target) {
        if (source == null || source.getSegments() == null || source.getSegments().isEmpty()) return;

        Duration totalLayover = Duration.ZERO;
        List<FlightDetailsResponse.FlightLeg> legs = target.getFlightLegs();
        List<TboApiFlightTicketResponseDto.Segment> segments = source.getSegments();

        for (int i = 0; i < segments.size(); i++) {
            TboApiFlightTicketResponseDto.Segment segment = segments.get(i);
            FlightDetailsResponse.FlightLeg leg = legs.get(i);

            if (i < segments.size() - 1) {
                String arrTime = segment.getDestination().getDepTime(); // Adjust if using ArrTime instead
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
        }

        target.setTotalLayover(Helper.getDurationString(totalLayover.toString()));
    }
}

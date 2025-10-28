package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingResponseNonLcc;
import com.jamuara.crs.flight.dto.tbo.book.TboApiFlightBookingResponseDto;
import com.jamuara.crs.flight.dto.tbo.book.TravelerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(config = CentralMapperConfig.class, imports = { LocalDateTime.class })
public interface TboFlightBookingResponseMapper {

    @Mapping(source = "traceId", target = "traceId")
    @Mapping(source = "response", target = "bookingDetails")
    FlightBookingResponseNonLcc mapToBookingResponse(TboApiFlightBookingResponseDto source);

    @Mapping(source = "pnr", target = "pnr")
    @Mapping(source = "bookingId", target = "bookingId")
    @Mapping(expression = "java(com.jamuara.crs.flight.dto.tbo.book.FlightBookingResponseNonLcc.BookingStatus.values()[source.getStatus()])", target = "bookingStatus")
    @Mapping(source = "priceChanged", target = "priceChanged")
    @Mapping(source = "timeChanged", target = "timeChanged")
    @Mapping(source = "flightItinerary", target = "flightDetails")
    FlightBookingResponseNonLcc.BookingDetails mapToBookingDetails(TboApiFlightBookingResponseDto.ResponseData source);

    @Mapping(source = "LCC", target = "LCC")
    @Mapping(source = "nonRefundable", target = "nonRefundable")
    @Mapping(source = "domestic", target = "domestic")
    @Mapping(source = "validatingAirlineCode", target = "validatingAirline")
    @Mapping(source = "airlineTollFreeNo", target = "airlineTollFreeNo")
    @Mapping(source = "lastTicketDate", target = "lastTicketDate")
    @Mapping(source = "fare", target = "fare")
    @Mapping(source = "passengers", target = "travelers")
    FlightBookingResponseNonLcc.BookFlightDetails mapToBookFlightDetails(TboApiFlightBookingResponseDto.FlightItinerary source);

    @Mapping(source = "paxId", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "email", target = "email")
    @Mapping(expression = "java(com.jamuara.crs.enums.Gender.values()[source.getGender() - 1])", target = "gender")
    @Mapping(expression = "java(com.jamuara.crs.enums.TravelerType.values()[source.getPaxType() - 1])", target = "travelerType")
    @Mapping(source = "leadPax", target = "lead")
    @Mapping(source = "cellCountryCode", target = "phoneCountryCode")
    @Mapping(source = "contactNo", target = "phone")
    @Mapping(expression = "java(mapPassportDetails(source))", target = "passportDetails")
    @Mapping(expression = "java(mapAddressDetails(source))", target = "address")
    @Mapping(source = "fare", target = "farePerTraveler")
    TravelerDto mapToTravelerDetails(TboApiFlightBookingResponseDto.Passenger source);

    default TravelerDto.IdentityDocument mapPassportDetails(TboApiFlightBookingResponseDto.Passenger passenger) {
        if(passenger == null) return null;

        TravelerDto.IdentityDocument passportDetails = new TravelerDto.IdentityDocument();
        passportDetails.setNumber(passenger.getPassportNo());
        passportDetails.setExpiryDate(passenger.getPassportExpiry());
        passportDetails.setNationality(passenger.getCountryCode());

        return passportDetails;
    }

    default TravelerDto.AddressDto mapAddressDetails(TboApiFlightBookingResponseDto.Passenger source) {
        TravelerDto.AddressDto addressDto = new TravelerDto.AddressDto();
        addressDto.setLine1(source.getAddressLine1());
        addressDto.setLine2(source.getAddressLine2());
        addressDto.setCity(source.getCity());
        addressDto.setCountry(source.getCountryName());
        addressDto.setCountryCode(source.getCountryCode());

        return addressDto;
    }

    @Mapping(source = "currency", target = "currency")
    @Mapping(expression = "java(String.valueOf(source.getBaseFare()))", target = "totalBaseFareAmount")
    @Mapping(expression = "java(String.valueOf(source.getTax()))", target = "totalTaxAmount")
    @Mapping(source = "taxBreakup", target = "taxBreakup")
    @Mapping(expression = "java(String.valueOf(source.getPgCharge()))", target = "pgCharge")
    @Mapping(expression = "java(String.valueOf(source.getYqTax()))", target = "yqTax")
    @Mapping(expression = "java(String.valueOf(source.getOtherCharges()))", target = "otherCharges")
    @Mapping(source = "chargeBU", target = "chargesBreakup")
    @Mapping(expression = "java(String.valueOf(source.getPublishedFare()))", target = "publishedFare")
    @Mapping(expression = "java(String.valueOf(source.getServiceFee()))", target = "serviceFee")
    @Mapping(expression = "java(String.valueOf(source.getTotalBaggageCharges()))", target = "baggageCharges")
    @Mapping(expression = "java(String.valueOf(source.getTotalMealCharges()))", target = "mealCharges")
    @Mapping(expression = "java(String.valueOf(source.getTotalSeatCharges()))", target = "seatCharges")
    @Mapping(expression = "java(String.valueOf(source.getTotalSpecialServiceCharges()))", target = "specialServiceCharges")
    FlightBookingResponseNonLcc.Fare mapToBookingFare(TboApiFlightBookingResponseDto.Fare source);

}

package com.jamuara.crs.hotel.mappers;

import com.amadeus.resources.HotelOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.model.HotelGuest;
import com.jamuara.crs.model.HotelReservation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface HotelReservationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplierBookingId", source = "id")
    @Mapping(target = "bookingStatus", expression = "java(getFirstBooking(order).getBookingStatus())")
    @Mapping(target = "hotelId", expression = "java(getFirstBooking(order).getHotel().getHotelId())")
    @Mapping(target = "hotelName", expression = "java(getFirstBooking(order).getHotel().getName())")
    @Mapping(target = "checkInDate", expression = "java(getFirstBooking(order).getHotelOffer().getCheckInDate())")
    @Mapping(target = "checkOutDate", expression = "java(getFirstBooking(order).getHotelOffer().getCheckOutDate())")
    @Mapping(target = "roomCount", expression = "java(getFirstBooking(order).getRoomAssociations().length)")
    @Mapping(target = "email", expression = "java(getFirstGuest(order).getEmail())")
    @Mapping(target = "phone", expression = "java(getFirstGuest(order).getPhone())")
    @Mapping(target = "totalPrice", expression = "java(getFirstBooking(order).getHotelOffer().getPrice().getTotal())")
    @Mapping(target = "currency", expression = "java(getFirstBooking(order).getHotelOffer().getPrice().getCurrency())")
    @Mapping(target = "supplier", constant = "AMADEUS")
    @Mapping(target = "bookingResponse", ignore = true)
    @Mapping(target = "bookingRequest", ignore = true)
    @Mapping(target = "guests", source = "guests")
    HotelReservation toEntity(HotelOrder order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tid", source = "tid")
    HotelGuest toHotelGuest(HotelOrder.Guest guest);

    List<HotelGuest> mapGuests(HotelOrder.Guest[] guests);

    default HotelOrder.HotelBooking getFirstBooking(HotelOrder order) {
        if (order.getHotelBookings() == null || order.getHotelBookings().length == 0) {
            return null;
        }
        return order.getHotelBookings()[0];
    }

    default HotelOrder.Guest getFirstGuest(HotelOrder order) {
        if(order.getGuests() == null || order.getGuests().length == 0) return null;

        return order.getGuests()[0];
    }

    @AfterMapping
    default void linkGuests(@MappingTarget HotelReservation reservation) {
        if (reservation.getGuests() != null) {
            reservation.getGuests().forEach(g -> g.setHotelReservation(reservation));
        }
    }

//    default String mapBookingResponse(HotelOrder order) {
//        try {
//            return new ObjectMapper().writeValueAsString(order);
//        } catch (Exception e) {
//            return null;
//        }
//    }
}

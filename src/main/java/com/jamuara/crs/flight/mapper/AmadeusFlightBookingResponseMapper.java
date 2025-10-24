package com.jamuara.crs.flight.mapper;
import com.amadeus.resources.FlightOrder;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class, uses = {
        AmadeusFlightTravelerRequestMapper.class,
        AmadeusFlightOfferMapper.class
})
public interface AmadeusFlightBookingResponseMapper {
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "travelers", target = "travelers")
    @Mapping(source = "flightOffers", target = "flightOffer")
//    @Mapping(target = "flightOffer", expression = "java(flightOrder.getFlightOffers()[0])")
    FlightBookingResponse toFlightBookingResponse(FlightOrder flightOrder);
}

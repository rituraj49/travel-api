package com.jamuara.crs.hotel.mappers;

import com.amadeus.resources.Hotel;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.hotel.model.HotelSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface HotelSearchResponseMapper {

    @Mapping(source = "iataCode", target = "iata")
    HotelSearchResponse toHotelSearchResponse(Hotel hotel);

    HotelSearchResponse.GeoCode toGeoCode(Hotel.GeoCode geoCode);

    @Mapping(target = "lines", expression = "java(java.util.Arrays.asList(address.getLines()))")
    HotelSearchResponse.Address toAddress(Hotel.Address address);

    HotelSearchResponse.Distance toDistance(Hotel.Distance distance);

}

package com.jamuara.crs.common.location.mapper;

import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.enums.LocationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface AmadeusLocationMapper {
    @Mapping(source = "subType", target = "subType")
    @Mapping(source = "iataCode", target = "iata")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "geoCode.latitude", target = "latitude")
    @Mapping(source = "geoCode.longitude", target = "longitude")
    @Mapping(source = "timeZoneOffset", target = "timeZoneOffset")
    @Mapping(source = "address.cityCode", target = "cityCode")
    @Mapping(source = "address.countryCode", target = "countryCode")
    @Mapping(source = "address.cityName", target = "city")
    Location toLocation(com.amadeus.resources.Location source);

    List<Location> toLocationList(List<com.amadeus.resources.Location> sourceList);

    default LocationType mapSubType(String subType) {
        return subType  != null ? LocationType.valueOf(subType) : null;
    }
}

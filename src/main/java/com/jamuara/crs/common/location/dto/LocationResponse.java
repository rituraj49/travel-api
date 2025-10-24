package com.jamuara.crs.common.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamuara.crs.enums.LocationType;
import com.jamuara.crs.enums.LocationTypeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {
    @CsvCustomBindByName(column = "type", converter = LocationTypeConverter.class)
    private LocationType subType;

    @CsvBindByName( column = "iata")
    private String iata;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "latitude")
    private Double latitude;

    @CsvBindByName(column = "longitude")
    private Double longitude;

    @CsvBindByName(column = "time_zone")
    @JsonProperty("timeZone")
    private String timeZoneOffset;

    @CsvBindByName(column = "city_code")
    @JsonProperty("cityCode")
    private String cityCode;

    @CsvBindByName(column = "country_code")
    @JsonProperty("countryCode")
    private String countryCode;

    @CsvBindByName(column = "city")
    private String city;

    @JsonProperty("groupData")
//    private SimpleAirportWrapper groupData;
    private List<SimpleAirport> groupData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleAirport {
        private LocationType subType;

        private String iata;

        private String name;

        private String city;

        @JsonProperty("cityCode")
        private String cityCode;

        @JsonProperty("countryCode")
        private String countryCode;
    }
}
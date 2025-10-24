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


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
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
//
//     @PostConstruct
//     public void  initLocation() {
//            this.location = new GeoPoint(latitude, longitude);
//     }
}


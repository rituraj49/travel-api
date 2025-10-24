package com.jamuara.crs.common;

import com.jamuara.crs.common.location.dto.CityGroup;
import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.common.location.dto.LocationResponse;
import com.jamuara.crs.enums.LocationType;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Helper {

    public static <T> List<T> convertCsv(Reader reader, Class<T> tClass) throws IOException {
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(tClass)
//                        .withSeparator('^')
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();
    }

    public static List<LocationResponse> getGroupedLocationData(List<Location> data) {
        Map<String, List<Location>> groupedData = data.stream().collect(Collectors.groupingBy(Location::getCityCode));

        List<LocationResponse> result = new ArrayList<>();

        for(Map.Entry<String, List<Location>> entry : groupedData.entrySet()) {
            List<Location> group = entry.getValue();

            Location airportCity = group.stream().filter(p -> LocationType.CITY.equals(p.getSubType())).findFirst()
                    .orElse(group.get(0));

//            Location airportCity = match.orElse(null);
//            if(airportCity != null) airportCity.setName("All airports within " + airportCity.getName());

//            if (airportCity == null) {
//                airportCity = group.get(0);
//            } else {
//                airportCity.setName("All airports within " + airportCity.getName());
//            }
            if(LocationType.CITY.equals(airportCity.getSubType())) {
                airportCity.setName("All airports within " + airportCity.getName());
            }

            List<LocationResponse.SimpleAirport> subAirports = group.
                    stream()
//                    .skip(1)
                    .filter(p -> !airportCity.getIata().equals(p.getIata()))
                    .map(c -> {
                        LocationResponse.SimpleAirport simpleAirport = new LocationResponse.SimpleAirport();
                        simpleAirport.setSubType(c.getSubType());
                        simpleAirport.setIata(c.getIata());
                        simpleAirport.setName(c.getName());
                        simpleAirport.setCityCode(c.getCityCode());
                        simpleAirport.setCountryCode(c.getCountryCode());
                        simpleAirport.setCity(c.getCity());
                        return simpleAirport;
                    })
                    .toList();

//            SimpleAirportWrapper children = new SimpleAirportWrapper();
//            children.setSimpleAirports(subAirports);

            LocationResponse locationResponse = getLocationResponse(airportCity, subAirports);
//            parent.setParent(airportCity);
//            parent.setGroupData(children);

            result.add(locationResponse);
        }
        return result;
    }

    private static LocationResponse getLocationResponse(Location location, List<LocationResponse.SimpleAirport> children) {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setSubType(location.getSubType());
        locationResponse.setIata(location.getIata());
        locationResponse.setName(location.getName());
        locationResponse.setLatitude(location.getLatitude());
        locationResponse.setLongitude(location.getLongitude());
        locationResponse.setTimeZoneOffset(location.getTimeZoneOffset());
        locationResponse.setCityCode(location.getCityCode());
        locationResponse.setCountryCode(location.getCountryCode());
        locationResponse.setCity(location.getCity());
        locationResponse.setGroupData(children);
        return locationResponse;
    }

    public static List<LocationResponse> getGroupedCityData(List<Location> airports, List<CityGroup> cityGroupList) {
        Map<String, List<Location>> airportsByCity = airports.stream()
                .collect(Collectors.groupingBy(Location::getCityCode));

        Map<String, CityGroup> cityGroupMap = cityGroupList.stream()
                .collect(Collectors.toMap(CityGroup::getCityCode, cg -> cg));

        return airportsByCity.entrySet().stream()
                .map(entry -> {
                    String cityCode = entry.getKey();
                    Location airportRep = entry.getValue().get(0);

                    LocationResponse response = new LocationResponse();
                    response.setIata(airportRep.getIata());
                    response.setCity(airportRep.getCity());
                    response.setCityCode(airportRep.getCityCode());
                    response.setLatitude(airportRep.getLatitude());
                    response.setLongitude(airportRep.getLongitude());
                    response.setSubType(airportRep.getSubType());
                    response.setName(airportRep.getName());
                    response.setCountryCode(airportRep.getCountryCode());
                    response.setCountryCode(airportRep.getCountryCode());
                    response.setTimeZoneOffset(airportRep.getTimeZoneOffset());

                    List<LocationResponse.SimpleAirport> subAirports = Optional.ofNullable(cityGroupMap.get(cityCode))
                            .map(cg -> cg.getAirportGroup().stream()
                                    .filter(sa -> !sa.getIata().equals(airportRep.getIata()))
                                    .sorted(Comparator.comparing((LocationResponse.SimpleAirport sa) ->
                                            "CITY".equalsIgnoreCase(String.valueOf(sa.getSubType())) ? 0 : 1))
                                    .toList())
                            .orElse(List.of());
//                    List<LocationResponse.SimpleAirport> finalSubAirports = new ArrayList<>(subAirports);
//                    SimpleAirportWrapper finalSubAirports = new SimpleAirportWrapper();
//                    finalSubAirports.setSimpleAirports(subAirports);
                    response.setGroupData(subAirports);

                    return response;
                }).toList();
    }

    public static String getEmailBody(TemplateEngine templateEngine, String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }

    public static String getDurationString(String duration) {
        if(duration == null || duration.isEmpty()) {
            return "0h 0m";
        }
        Duration dur = Duration.parse(duration);
        long hrs = dur.toHours();
        long min = dur.minusHours(hrs).toMinutes();
        return String.format("%dh %dm", hrs, min);
    }
}

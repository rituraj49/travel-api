package com.jamuara.crs.flight.mapper;

import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.tbo.FlightFareRuleResponse;
import com.jamuara.crs.flight.dto.tbo.TboApiFareRuleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.time.LocalDateTime;

@Mapper(config = CentralMapperConfig.class, imports = {LocalDateTime.class, Duration.class})
public interface TboFlightFareRulesMapper {
    @Mapping(source = "traceId", target = "traceId")
    @Mapping(source = "fareRules", target = "fareRules")
    FlightFareRuleResponse mapToFareRulesResponse(TboApiFareRuleResponseDto.Response source);

    @Mapping(source = "airline", target = "airline")
    @Mapping(source = "origin", target = "origin")
    @Mapping(source = "destination", target = "destination")
    @Mapping(source = "fareBasisCode", target = "fareBasisCode")
    @Mapping(source = "fareInclusions", target = "fareInclusions")
    @Mapping(source = "fareRestriction", target = "fareRestriction")
    @Mapping(source = "fareRuleDetail", target = "fareRuleDetail")
    FlightFareRuleResponse.FareRule mapToFareRule(TboApiFareRuleResponseDto.FareRule source);
}

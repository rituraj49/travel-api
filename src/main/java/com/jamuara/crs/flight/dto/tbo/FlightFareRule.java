package com.jamuara.crs.flight.dto.tbo;

import lombok.Data;

import java.util.List;

@Data
public class FlightFareRule {
    private String origin;
    private String destination;
    private String airline;
    private String fairBasisCode;
    private String fairRuleDetail;
    private List<String> fareInclusions;
}

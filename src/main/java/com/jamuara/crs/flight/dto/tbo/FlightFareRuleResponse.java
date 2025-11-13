package com.jamuara.crs.flight.dto.tbo;

import lombok.Data;

import java.util.List;

@Data
public class FlightFareRuleResponse {
    private String traceId;

    private List<FareRule> fareRules;

    @Data
    public static class FareRule {
        private String airline;

        private String origin;

        private String destination;

        private String fareBasisCode;

        private List<String> fareInclusions;

        private String fareRestriction;

        private String fareRuleDetail;
    }
}

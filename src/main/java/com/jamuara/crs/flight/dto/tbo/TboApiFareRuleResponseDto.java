package com.jamuara.crs.flight.dto.tbo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TboApiFareRuleResponseDto {
    @JsonProperty("Response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("Error")
        private ErrorDetails error;

        @JsonProperty("FareRules")
        private List<FareRule> fareRules;

        @JsonProperty("ResponseStatus")
        private int responseStatus;

        @JsonProperty("TraceId")
        private String traceId;
    }

    @Data
    public static class ErrorDetails {
        @JsonProperty("ErrorCode")
        private int errorCode;

        @JsonProperty("ErrorMessage")
        private String errorMessage;
    }

    @Data
    public static class FareRule {
        @JsonProperty("Airline")
        private String airline;

        @JsonProperty("DepartureTime")
        private String departureTime;

        @JsonProperty("Destination")
        private String destination;

        @JsonProperty("FareBasisCode")
        private String fareBasisCode;

        @JsonProperty("FareInclusions")
        private List<String> fareInclusions;

        @JsonProperty("FareRestriction")
        private String fareRestriction;

        @JsonProperty("FareRuleDetail")
        private String fareRuleDetail;

        @JsonProperty("FlightId")
        private int flightId;

        @JsonProperty("Origin")
        private String origin;

        @JsonProperty("ReturnDate")
        private String returnDate;
    }
}

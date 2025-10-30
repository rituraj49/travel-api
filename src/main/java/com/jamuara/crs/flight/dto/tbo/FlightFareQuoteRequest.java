package com.jamuara.crs.flight.dto.tbo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlightFareQuoteRequest {
    @Schema(name = "traceId")
    private String traceId;

    @Schema(name = "inboundResultIndex")
    private String resultIndexOutbound;

    @Schema(name = "outboundResultIndex")
    private String resultIndexInbound;
}

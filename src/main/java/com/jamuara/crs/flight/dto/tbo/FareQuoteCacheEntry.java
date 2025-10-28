package com.jamuara.crs.flight.dto.tbo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FareQuoteCacheEntry {
    private Object outboundFlight;
    private Object inboundFlight;
}

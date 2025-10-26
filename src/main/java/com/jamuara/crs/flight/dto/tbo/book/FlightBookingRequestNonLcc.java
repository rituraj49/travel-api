package com.jamuara.crs.flight.dto.tbo.book;

import com.jamuara.crs.enums.Gender;
import com.jamuara.crs.enums.TravelerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FlightBookingRequestNonLcc {
    private String traceId;

    private String resultIndex;

    private List<TravelerDto> travelers;
}

package com.jamuara.crs.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "HotelSearchRequest", description = "Request object for searching hotel offers")
public class HotelSearchRequestDto {

    @Schema(description = "IATA city code", example = "NYC", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cityCode;

    @Schema(description = "Search radius around city", example = "10")
    private int radius;

    @Schema(description = "Radius unit (KM or MI)", example = "KM")
    private String radiusUnit;

    @Schema(description = "List of required amenities",
            example = "[\"WIFI\", \"POOL\"]")
    private List<String> amenities;

    @Schema(description = "Number of guests",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private int guests;

    @Schema(description = "Check-in date (YYYY-MM-DD)",
            example = "2026-03-01",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String checkInDate;

    @Schema(description = "Check-out date (YYYY-MM-DD)",
            example = "2026-03-05",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String checkOutDate;

    @Schema(description = "Residence country (ISO 3166-1 alpha-2)",
            example = "US")
    private String residenceCountry;

    @Schema(description = "Number of rooms",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private int roomQuantity;

    @Schema(description = "Price range (e.g. 100-300)",
            example = "100-300")
    private String priceRange;

    @Schema(description = "Currency code (ISO 4217)",
            example = "USD")
    private String currency;

    @Schema(description = "Return only best rate offers",
            example = "true")
    private boolean bestRateOnly;

    @Schema(description = "Response language (ISO 639-1)",
            example = "en")
    private String lang;
}

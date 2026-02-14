package com.jamuara.crs.common.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OSMLocationResponse {
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    private String type;
    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("lon")
    private String longitude;
}

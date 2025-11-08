package com.jamuara.crs.flight.dto.tbo.search;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class FareClassificationDeserializer extends JsonDeserializer<TboApiFlightResponseDto.Response.FareClassification> {
    @Override
    public TboApiFlightResponseDto.Response.FareClassification deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
//        if(p.getCurrentToken().isStructStart()) {
        if(p.getCurrentToken() == JsonToken.START_OBJECT) {
            JsonNode node = p.readValueAsTree();

            TboApiFlightResponseDto.Response.FareClassification fareClassification = new TboApiFlightResponseDto.Response.FareClassification();

            if(node.has("Color")) {
                fareClassification.setColor(node.get("Color").asText());
            }

            if(node.has("Type")) {
                fareClassification.setType(node.get("Type").asText());
            }

            return fareClassification;
//            return p.readValueAs(TboApiFlightResponseDto.Response.FareClassification.class);
        } else if(p.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        } else {
            System.out.println("unexpected value for fare classification in the json resource" + p.getText());
            throw new IOException("Unexpected value for the FareClassification");
        }
    }
}

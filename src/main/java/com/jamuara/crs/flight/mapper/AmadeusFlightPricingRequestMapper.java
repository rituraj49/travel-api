package com.jamuara.crs.flight.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmadeusFlightPricingRequestMapper {
    public static String mapToPricingRequestString(String offerString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> flightOfferMap = mapper.readValue(offerString, new TypeReference<>() {});
//
//        Map<String, Object> dataWrapper = new HashMap<>();
//        dataWrapper.put("type", "flight-offers-pricing");
//        dataWrapper.put("flightOffers", List.of(flightOfferMap));
//
//        Map<String, Object> root = new HashMap<>();
//        root.put("data", dataWrapper);
//
//        String wrappedString = mapper.writeValueAsString(root);
//        return wrappedString;

        JsonNode flightOfferNode = mapper.readTree(offerString);

        ObjectNode dataWrapper = mapper.createObjectNode();
        dataWrapper.put("type", "flight-offers-pricing");
        dataWrapper.set("flightOffers", mapper.createArrayNode().add(flightOfferNode));

        ObjectNode root = mapper.createObjectNode();
        root.set("data", dataWrapper);

        return mapper.writeValueAsString(root);
    }
}

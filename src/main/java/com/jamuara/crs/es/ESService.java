package com.jamuara.crs.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.location.dto.CityGroup;
import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.common.location.dto.LocationResponse;
import com.jamuara.elasticlib.service.BulkItemMapper;
import com.jamuara.elasticlib.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ESService {

    private final ElasticsearchClient esClient;

    @Autowired
    private com.jamuara.elasticlib.service.ElasticSearchService jamuaraElasticSearchService;

    public ESService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public void createIndex(String indexName) {
        try {
            boolean exists = esClient.indices().exists(e -> e.index("airports")).value();

            if(exists) {
                log.warn("index already exists");
                return;
            }

            CreateIndexResponse response =
                    esClient.indices().create(cl -> cl
                            .index(indexName)
                            .settings(s -> s
                                    .analysis(an -> an
                                            .analyzer("autocomplete", a -> a
                                                    .custom(c -> c
                                                            .tokenizer("whitespace")
                                                            .filter("lowercase", "autocomplete_filter")
                                                    )
                                            )
                                            .filter("autocomplete_filter", f -> f
                                                    .definition(df -> df
                                                            .edgeNgram(en -> en
                                                                    .minGram(2)
                                                                    .maxGram(20)
                                                            )
                                                    )
                                            )
                                    )
                            )
                            .mappings(m -> m
                                    .properties("code", p -> p.keyword(k -> k))
                                    .properties("icao", p -> p.keyword(k -> k))
                                    .properties("name", p -> p.text(k -> k
                                            .analyzer("autocomplete")))
                                    .properties("latitude", p -> p.double_(k -> k
                                            .fields("raw", t -> t.keyword(r -> r))
                                    ))
                                    .properties("longitude", p -> p.double_(k -> k
                                            .fields("raw", t -> t.keyword(r -> r))
                                    ))
                                    .properties("elevation", p -> p.integer(k -> k))
                                    .properties("url", p -> p.text(k -> k))
                                    .properties("time_zone", p -> p.text(k -> k))
                                    .properties("city_code", p -> p.keyword(k -> k))
                                    .properties("country_code", p -> p.keyword(k -> k))
                                    .properties("city", p -> p.text(k -> k
                                            .analyzer("autocomplete")
                                            .fields("raw", at -> at.keyword(kv -> kv))
                                    ))
                                    .properties("state", p -> p.text(k -> k
                                            .analyzer("autocomplete")
                                            .fields("raw", at -> at.keyword(kv -> kv))
                                    ))
                                    .properties("county", p -> p.text(k -> k
                                            .analyzer("autocomplete")
                                            .fields("raw", at -> at.keyword(kv -> kv))
                                    ))
                            ));
            log.info("index {} created successfully", indexName);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException("failed to create index", e);
        }
    }

    public void jamuaraBulkUpload(List<Location> locations, String indexName) throws IOException {
        jamuaraElasticSearchService.bulkUploadData(locations, indexName,
                new BulkItemMapper<Location>() {
            @Override
            public String getId(Location location) {
                return location.getIata();
            }

            @Override
            public Map<String, Object> map(Location location) {
                return toLocationDocument(location);
            }
        },
1000);
    }

    private void jamuaraIndexCityGroups(List<Location> locations) throws IOException {
        Map<String, List<Location>> cityGroups = locations.stream().collect(Collectors.groupingBy(Location::getCityCode));

        List<CityGroup> cityGroupList = cityGroups.entrySet().stream().map(c -> {
            CityGroup cityGroup = new CityGroup();
            cityGroup.setCityCode(c.getKey());
            List<LocationResponse.SimpleAirport> simpleAirportList = c.getValue().stream()
                    .map(a -> new LocationResponse.SimpleAirport(
                            a.getSubType(),
                            a.getIata(),
                            a.getName(),
                            a.getCity(),
                            a.getCityCode(),
                            a.getCountryCode()
                    )).toList();
            cityGroup.setAirportGroup(simpleAirportList);
            return cityGroup;
        }).toList();

        jamuaraElasticSearchService.bulkUploadData(cityGroupList, "city_groups",
                new BulkItemMapper<CityGroup>() {
                    @Override
                    public String getId(CityGroup cityGroup) {
                        return cityGroup.getCityCode();
                    }

                    @Override
                    public Map<String, Object> map(CityGroup cityGroup) {
                        Map<String, Object> cgMap = new HashMap<>();

                        cgMap.put("city_code", cityGroup.getCityCode());
                        cgMap.put("airports", cityGroup.getAirportGroup());
                        return cgMap;
                    }
                }, cityGroups.size());
    }

    public void bulkUpload(List<Location> locations, String indexName) throws IOException {
        int batchSize = 1000;
        int total = locations.size();
        Map<String, List<Location>> cityGroups = locations.stream().collect(Collectors.groupingBy(Location::getCityCode));

        indexCityGroups(cityGroups, esClient);

        for(int i = 0; i <= total; i += batchSize) {
            int end = Math.min(i + batchSize, total);

            List<Location> batch = locations.subList(i, end);
            if(batch.isEmpty()) continue;

            BulkRequest.Builder br = getBuilder(indexName, batch);
            BulkResponse response = esClient.bulk(br.build());

            if(response.errors()) {
                log.error("errors occurred in batch from: {} to {}", i, end - 1);
            } else {
                log.info("successfully inserted batch from: {} to {}", i, end-1);
            }
        }
    }

    private static void indexCityGroups(Map<String, List<Location>> cityGroups, ElasticsearchClient esClient) throws IOException {
        List<CityGroup> cityGroupList = cityGroups.entrySet().stream().map(c -> {
            CityGroup cityGroup = new CityGroup();
            cityGroup.setCityCode(c.getKey());
            List<LocationResponse.SimpleAirport> simpleAirportList = c.getValue().stream()
                    .map(a -> new LocationResponse.SimpleAirport(
                        a.getSubType(),
                        a.getIata(),
                        a.getName(),
                        a.getCity(),
                        a.getCityCode(),
                        a.getCountryCode()
                    )).toList();
            cityGroup.setAirportGroup(simpleAirportList);
            return cityGroup;
        }).toList();

        BulkRequest.Builder br = new BulkRequest.Builder();

        for(CityGroup cg: cityGroupList) {
            br.operations(op -> op
                .index(idx -> idx
                    .index("city_groups")
                    .id(cg.getCityCode())
                    .document(cg)
                )
            );
        }

        BulkResponse bulkResponse = esClient.bulk(br.build());
    }

    private static BulkRequest.Builder getBuilder(String indexName,
                                                  List<Location> batch) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for(Location a: batch) {
            br.operations(op -> op
                .index(idx -> idx
                        .index(indexName)
                        .id(a.getIata())
                        .document(toLocationDocument(a)
                    )
                )
            );
        }
        return br;
    }

    public static Map<String, Object> toLocationDocument(Location airport) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("subType", airport.getSubType().toString());
        doc.put("iata", airport.getIata());
        doc.put("name", airport.getName());
        doc.put("latitude", airport.getLatitude());
        doc.put("longitude", airport.getLongitude());
        doc.put("time_zone", airport.getTimeZoneOffset());
        doc.put("city_code", airport.getCityCode());
        doc.put("country_code", airport.getCountryCode());
        doc.put("city", airport.getCity());
//        doc.put("group_data", airport.getGroupData());

        if(airport.getLatitude() != null && airport.getLongitude() != null) {
            doc.put("location", Map.of(
                    "lat", airport.getLatitude(),
                    "lon", airport.getLongitude()
            ));
        }
        return doc;
    }

    public List<LocationResponse> searchByKeyword(String keyword, int page, int size) throws IOException {
        log.info("cache missed: calling search elastic index for keyword: {}, page: {}, size: {}", keyword, page, size);
        int from = (page - 1) * size;
        SearchResponse<Location> sr = esClient.search(s -> s
                        .index("airports")
                        .from(from)
                        .size(size)
                        .query(q -> q
                                .bool(b -> b
                                        .should(sh -> sh.term(t -> t.field("iata.raw").value(keyword).boost(5f)))
                                        .should(sh -> sh.term(t -> t.field("city_code.raw").value(keyword).boost(3f)))
                                        .should(sh -> sh.match(m -> m.field("name").query(keyword).boost(2f)))
                                        .should(sh -> sh.match(m -> m.field("city").query(keyword).boost(1f)))
                                        .minimumShouldMatch("1")
                                )
                        )
                        .sort(
                                so -> so.score(o -> o.order(SortOrder.Desc)
                                )
                        ),
                Location.class
        );

        List<Hit<Location>> hits = sr.hits().hits();

        List<Location> airports = new ArrayList<>();

//      sorting by relevancy in descending order
//        List<Location> airports = hits.stream().sorted((h1, h2) -> Double.compare(
//                        Optional.ofNullable(h2.score()).orElse(0.0),
//                        Optional.ofNullable(h1.score()).orElse(0.0)
//                ))
//                .map(Hit::source)
//                .toList();

        for(Hit<Location> hit: hits) {
            airports.add(hit.source());
        }

        Set<String> cityCodes = airports.stream().map(Location::getCityCode).collect(Collectors.toSet());
        SearchResponse<CityGroup> cityGroupSearchResponse = esClient.search(s -> s
                        .index("city_groups")
                        .query(q -> q
                                .terms(t -> t
                                        .field("city_code.keyword")
                                        .terms(tq -> tq
                                                .value(cityCodes.stream()
                                                        .map(FieldValue::of)
                                                        .collect(Collectors.toList()))
                                        )
                                )
                        ),
                CityGroup.class
        );
        List<CityGroup> cityGroupList = new ArrayList<>();
        List<Hit<CityGroup>> cityGroupHits = cityGroupSearchResponse.hits().hits();

        for(Hit<CityGroup> c: cityGroupHits) {
            cityGroupList.add(c.source());
        }
        List<LocationResponse> locationResponseList = Helper.getGroupedCityData(airports, cityGroupList);
        return locationResponseList;
//        LocationResponseWrapper wrapper = new LocationResponseWrapper();
//        wrapper.setLocationResponses(locationResponseList);
//        return wrapper;
    }

    public List<LocationResponse> jamuaraElasticSearch(String keyword, String indexName, int page, int size) throws IOException {
        List<Location> airports = jamuaraElasticSearchService.search(
                indexName,
                s -> {
                    ElasticSearchService.applyPagination(s, page, size);

                    s.query(q -> q
                            .bool(b -> b
                                    .should(sh -> sh.term(t -> t.field("iata.raw").value(keyword).boost(5f)))
                                    .should(sh -> sh.term(t -> t.field("city_code.raw").value(keyword).boost(3f)))
                                    .should(sh -> sh.match(m -> m.field("name").query(keyword).boost(2f)))
                                    .should(sh -> sh.match(m -> m.field("city").query(keyword).boost(1f)))
                                    .minimumShouldMatch("1")
                            )
                    );
                    s.sort(so -> so.score(o -> o.order(SortOrder.Desc)));
                },
                Location.class
        );

        Set<String> cityCodes = airports.stream()
                .map(Location::getCityCode)
                .collect(Collectors.toSet());

        List<CityGroup> cityGroups = jamuaraElasticSearchService.search(
                "city_groups",
                s -> s.query(q -> q
                        .terms(t -> t
                                .field("city_code.keyword")
                                .terms(tq -> tq.value(
                                        cityCodes.stream()
                                                .map(FieldValue::of)
                                                .toList()
                                ))
                        )
                ),
                CityGroup.class
        );

        List<LocationResponse> response =
                Helper.getGroupedCityData(airports, cityGroups);

        return response;
    }
}

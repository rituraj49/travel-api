package com.jamuara.crs.common.location.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.location.dto.CityGroup;
import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.common.location.dto.LocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("esLocationService")
@Slf4j
public class ESLocationSearchService implements ISearchService {

    private final ElasticsearchClient esClient;

    public ESLocationSearchService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public List<LocationResponse> keywordSearch(String keyword) {
        int page = 1;
        int size = 10;
        log.info("cache missed: calling search elastic index for keyword: {}, page: {}, size: {}", keyword, page, size);
        int from = (page - 1) * size;
        SearchResponse<Location> sr = null;
        try {
            sr = esClient.search(s -> s
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        SearchResponse<CityGroup> cityGroupSearchResponse = null;
        try {
            cityGroupSearchResponse = esClient.search(s -> s
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}

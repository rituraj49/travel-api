package com.jamuara.crs.common.location.service;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.config.LuceneIndexInitializer;
import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.common.location.dto.LocationResponse;
import com.jamuara.crs.config.lucene.EdgeNGramAnalyzer;
import com.jamuara.crs.config.lucene.IndexingKeywordAnalyzer;
import com.jamuara.crs.config.lucene.SearchAnalyzer;
import com.jamuara.crs.enums.LocationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Primary
//@Profile("nodb")
public class LuceneSearchService implements ISearchService {

    private final LuceneIndexInitializer luceneIndexInitializer;

    public LuceneSearchService(LuceneIndexInitializer luceneIndexInitializer) {
        this.luceneIndexInitializer = luceneIndexInitializer;
    }

    @Override
    public List<LocationResponse> keywordSearch(String keyword) {
        List<Location> results = new ArrayList<>();

        try (DirectoryReader reader = DirectoryReader.open(luceneIndexInitializer.getInMemoryIndex())) {
            IndexSearcher searcher = new IndexSearcher(reader);
            String[] edgeFields = {"name", "city"};
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("city", 5.0f);
            boosts.put("name", 2.0f);
            boosts.put("iata", 1.0f);

            List<Query> exactQueries = List.of(
                    new TermQuery(new Term("iata", keyword.toLowerCase())),
//            	    new TermQuery(new Term("icao", keyword.toLowerCase())),
                    new TermQuery(new Term("city_code", keyword.toLowerCase()))
            );
            BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();

            Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(
                    new StandardAnalyzer(),
                    Map.of(
                            "iata", new IndexingKeywordAnalyzer(),
                            "icao", new IndexingKeywordAnalyzer(),
                            "city_code", new IndexingKeywordAnalyzer(),
                            "name", new SearchAnalyzer(),
                            "name_autocomplete", new EdgeNGramAnalyzer(),
                            "city", new SearchAnalyzer(),
                            "city_autocomplete", new EdgeNGramAnalyzer()
                    )
            );
            MultiFieldQueryParser mfqParser = new MultiFieldQueryParser(edgeFields, perFieldAnalyzer);

            mfqParser.setDefaultOperator(QueryParser.Operator.AND);

            Query edgeQuery = mfqParser.parse(QueryParserBase.escape(keyword.toLowerCase()));

            finalQuery.add(new BoostQuery(edgeQuery, 1.0f), BooleanClause.Occur.SHOULD);

            Query cityAutocompleteQuery = new PrefixQuery(new Term("city_autocomplete", keyword.toLowerCase()));
            Query nameAutocompleteQuery = new PrefixQuery(new Term("name_autocomplete", keyword.toLowerCase()));

            finalQuery.add(new BoostQuery(cityAutocompleteQuery, 1.0f), BooleanClause.Occur.SHOULD);
            finalQuery.add(new BoostQuery(nameAutocompleteQuery, 1.0f), BooleanClause.Occur.SHOULD);

            DisjunctionMaxQuery disjunctionMaxQuery = new DisjunctionMaxQuery(exactQueries, 0.0f);
            exactQueries.forEach(q -> finalQuery.add(new BoostQuery(disjunctionMaxQuery, 100.0f), BooleanClause.Occur.SHOULD));

//            TopDocs initialHits = searcher.search(query, 10);
            TopDocs initialHits = searcher.search(finalQuery.build(), 10);
            for (ScoreDoc scoreDoc : initialHits.scoreDocs) {
                Document doc = searcher.storedFields().document(scoreDoc.doc);

                results.add(new Location(
                        LocationType.valueOf(doc.get("subType")),
                        doc.get("iata"),
                        doc.get("name"),
                        Double.parseDouble(doc.get("latitude")),
                        Double.parseDouble(doc.get("longitude")),
                        doc.get("time_zone_offset"),
                        doc.get("city_code"),
                        doc.get("country_code"),
                        doc.get("city")
                ));
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

//        return results;
//		return HelperUtil.getGroupedData(results);
        List<LocationResponse> locationResponseList = Helper.getGroupedLocationData(results);
        return locationResponseList;
//        return new LocationResponseWrapper(locationResponseList);
    }
}

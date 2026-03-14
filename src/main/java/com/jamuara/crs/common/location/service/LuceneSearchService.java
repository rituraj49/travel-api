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
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
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

//        SearcherManager
        try (DirectoryReader reader = DirectoryReader.open(luceneIndexInitializer.getInMemoryIndex())) {
//        try (SearcherManager manager = new SearcherManager(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            String[] edgeFields = {"name_search", "city_search"};
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("city", 5.0f);
            boosts.put("iata", 1.0f);
            boosts.put("name", 2.0f);

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
//                            "name_autocomplete", new EdgeNGramAnalyzer(),
                            "name_autocomplete", new SearchAnalyzer(),
                            "city", new SearchAnalyzer(),
//                            "city_autocomplete", new EdgeNGramAnalyzer()
                            "city_autocomplete", new SearchAnalyzer()
                    )
            );
//            MultiFieldQueryParser mfqParser = new MultiFieldQueryParser(edgeFields, perFieldAnalyzer);
//            MultiFieldQueryParser mfqParser = new MultiFieldQueryParser(edgeFields, perFieldAnalyzer, boosts);
//            Query edgeQuery = mfqParser.parse(QueryParserBase.escape(keyword.toLowerCase()));
//            mfqParser.setDefaultOperator(QueryParser.Operator.AND);

            QueryParser qParser = new MultiFieldQueryParser(edgeFields, new SearchAnalyzer());
            qParser.setDefaultOperator(QueryParser.Operator.AND);

            Query textQuery = qParser.parse(QueryParserBase.escape(keyword.toLowerCase()));

            finalQuery.add(new BoostQuery(textQuery, 50f), BooleanClause.Occur.SHOULD);

            if(keyword.length() >= 3) {
                Query cityAutocompleteQuery = new PrefixQuery(new Term("city_autocomplete", keyword.toLowerCase()));
                Query nameAutocompleteQuery = new PrefixQuery(new Term("name_autocomplete", keyword.toLowerCase()));

                finalQuery.add(new BoostQuery(cityAutocompleteQuery, 200f), BooleanClause.Occur.SHOULD);
                finalQuery.add(new BoostQuery(nameAutocompleteQuery, 150f), BooleanClause.Occur.SHOULD);
            }

            finalQuery.add(new BoostQuery(new TermQuery(
                    new Term("iata", keyword.toUpperCase())), 2000f), BooleanClause.Occur.SHOULD);

            finalQuery.add(new BoostQuery(
                    new TermQuery(new Term("city_code", keyword.toUpperCase())), 1000f), BooleanClause.Occur.SHOULD);

            log.info("final query for search: {}", finalQuery.build());


//            TopDocs initialHits = searcher.search(query, 10);
            TopDocs initialHits = searcher.search(finalQuery.build(), 10);
            for (ScoreDoc scoreDoc : initialHits.scoreDocs) {
//                explanation for each result
//                Explanation explanation = searcher.explain(finalQuery.build(), scoreDoc.doc);
                Document doc = searcher.storedFields().document(scoreDoc.doc);

//                System.out.println("///////////////");
//                for(IndexableField f: doc.getFields()) {
//                    System.out.println(f.name() + " = " + f.stringValue());
//                }
//                System.out.println("--------------");

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

//            Terms terms = MultiTerms.getTerms(reader, "city_code");
//            TermsEnum termsEnum = terms.iterator();
//
//            int counter = 0;
//            BytesRef term;
//            while ((term = termsEnum.next()) != null && counter < 50) {
//                System.out.println("term: " + term.utf8ToString());
//                counter++;
//            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

//        return results;
//		return HelperUtil.getGroupedData(results);
        List<LocationResponse> locationResponseList = Helper.getGroupedLocationData(results);
        return locationResponseList;
//        return results;
//        return new LocationResponseWrapper(locationResponseList);
    }
}

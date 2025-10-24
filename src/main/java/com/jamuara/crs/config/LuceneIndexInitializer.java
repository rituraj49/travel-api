package com.jamuara.crs.config;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.location.dto.Location;
import com.jamuara.crs.config.lucene.EdgeNGramAnalyzer;
import com.jamuara.crs.config.lucene.IndexingKeywordAnalyzer;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Service
@Slf4j
public class LuceneIndexInitializer {

	private Directory inMemoryIndex;
	private Analyzer analyzer;

//	Logger logger = LoggerFactory.getLogger(InMemoryLuceneService.class);

	@PostConstruct
	public void init() {
		try {
//			this.inMemoryIndex = FSDirectory.open(Paths.get("airports"));
			this.inMemoryIndex = new ByteBuffersDirectory();
			this.analyzer = buildPerFieldAnalyzer();
			indexData();
		} catch (Exception e) {
			log.error("Error initializing Lucene: {}", e.getMessage());
//			e.printStackTrace();
			throw new RuntimeException("Lucene initialization failed", e);
		}
	}

    public Analyzer buildPerFieldAnalyzer() {
		Map<String, Analyzer> analyzerPerField = new HashMap<>();
		analyzerPerField.put("iata", new IndexingKeywordAnalyzer());
		analyzerPerField.put("icao", new IndexingKeywordAnalyzer());
		analyzerPerField.put("name_autocomplete", new EdgeNGramAnalyzer());
		analyzerPerField.put("name", new StandardAnalyzer());
		analyzerPerField.put("city_code", new IndexingKeywordAnalyzer());
		analyzerPerField.put("city_autocomplete", new EdgeNGramAnalyzer());
		analyzerPerField.put("city", new StandardAnalyzer());
		analyzerPerField.put("state", new EdgeNGramAnalyzer());

		Analyzer defaultAnalyzer = new StandardAnalyzer();

		return new PerFieldAnalyzerWrapper(defaultAnalyzer, analyzerPerField);
	}

	public List<Location> readDataFromFile() throws IOException {
		log.info("reading data from file...");
//		Path path = Paths.get("airports.csv");
//		try(Reader reader = Files.newBufferedReader(path)) {
		Resource resource = new ClassPathResource("data/airports.csv");
		try(Reader reader = new InputStreamReader(resource.getInputStream())) {
			return Helper.convertCsv(reader, Location.class);
		} catch (IOException e) {
//			e.printStackTrace();
			log.error("Error reading data from file: {}; {}", e.getCause(), e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public void indexData() throws IOException {
		log.info("Indexing data start...");
		List<Location> dataList = readDataFromFile();
		try(IndexWriter writer = new IndexWriter(inMemoryIndex, new IndexWriterConfig(analyzer))) {
			int batchSize = 1000;
			int total = dataList.size();
//        	int total = 10;

			for(int i = 0; i <= total; i += batchSize) {
				int end = Math.min(i + batchSize, total);

				List<Location> batch = dataList.subList(i, end);
				if(batch.isEmpty()) {
					continue;
				}
				for(Location a: batch) {
					Document doc = new Document();
					doc.add(new TextField("subType", a.getSubType().toString(), Field.Store.YES));
					doc.add(new TextField("iata", a.getIata(), Field.Store.YES));
					//        			doc.add(new TextField("icao", a.getIcao(), Field.Store.YES));
					doc.add(new TextField("name", a.getName(), Field.Store.YES));
					doc.add(new TextField("name_autocomplete", a.getName(), Field.Store.YES));
					doc.add(new DoubleField("latitude", a.getLatitude(), Field.Store.YES));
					doc.add(new DoubleField("longitude", a.getLongitude(), Field.Store.YES));
					//        			doc.add(new IntField("elevation", a.getElevation(), Field.Store.YES));
					//        			doc.add(new TextField("url", a.getUrl(), Field.Store.YES));
//	        			doc.add(new TextField("time_zone", a.getTime_zone(), Field.Store.YES));
					doc.add(new TextField("time_zone_offset", a.getTimeZoneOffset(), Field.Store.YES));
					doc.add(new TextField("city_code", a.getCityCode(), Field.Store.YES));
					doc.add(new TextField("country_code", a.getCountryCode(), Field.Store.YES));
					doc.add(new TextField("city", a.getCity(), Field.Store.YES));
					doc.add(new TextField("city_autocomplete", a.getCity(), Field.Store.YES));
//        			doc.add(new TextField("state", a.getState(), Field.Store.YES));
//        			doc.add(new TextField("county", a.getCounty(), Field.Store.YES));
//        			doc.add(new TextField("type", a.getType(), Field.Store.YES));
					writer.addDocument(doc);
				}
				log.info("added docs from {} to {}", i, end);
			}
		}
	}

}

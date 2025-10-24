package com.jamuara.crs.config.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;

public class IndexingKeywordAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		Tokenizer tokenizer = new KeywordTokenizer();
		TokenStream tokenStream = new LowerCaseFilter(tokenizer);
		
		return new TokenStreamComponents(tokenizer, tokenStream);
	}

}

package com.jamuara.crs.config.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;

public class EdgeNGramAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		Tokenizer tokenizer = new EdgeNGramTokenizer(1, 20);
		TokenStream tokenStream = new LowerCaseFilter(tokenizer);
		
		return new TokenStreamComponents(tokenizer, tokenStream);
	}

}

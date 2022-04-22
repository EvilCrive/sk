/*
 * Copyright 2021-2022 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.se.analyzers;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.IOException;
import java.io.Reader;

import static it.unipd.dei.se.analyzers.AnalyzerUtil.consumeTokenStream;
import static it.unipd.dei.se.analyzers.AnalyzerUtil.loadStopList;

/**
 * Basic {@link Analyzer} by using different {@link Tokenizer}s and {@link
 * org.apache.lucene.analysis.TokenFilter}s.
 *
 * @author Alberto Crivellari 
 * @version 1.0
 * @since 1.0
 */
public class BasicAnalyzer extends Analyzer {
	/**
	 * Creates a new instance of the analyzer.
	 */
    public BasicAnalyzer() {
        super();
    }

    private TokenStream addFilters(Tokenizer source) {
        TokenStream tokens;
        tokens = new LowerCaseFilter(source);
        //tokens = new NGramTokenFilter(tokens, 3);
        tokens = new StopFilter(tokens, AnalyzerUtil.loadStopList("99webtools.txt"));
        //tokens = new EnglishMinimalStemFilter(tokens);
        //tokens = new EnglishPossessiveFilter(tokens);
		//tokens = new LengthFilter(tokens, 4, 10);
		//tokens = new PorterStemFilter(tokens);
		//tokens = new KStemFilter(tokens);
		//tokens = new LovinsStemFilter(tokens);
		tokens = new NGramTokenFilter(tokens, 3);
		//tokens = new ShingleFilter(tokens, 2);

        return tokens;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        //final Tokenizer source = new WhitespaceTokenizer();
		//final Tokenizer source = new LetterTokenizer();
        final Tokenizer source = new StandardTokenizer();

        TokenStream tokens = addFilters(source);        

        return new TokenStreamComponents(source, tokens);
    }
    
    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        // return new HTMLStripCharFilter(reader);
        return super.initReader(fieldName, reader);
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }

	/**
	 * Main method of the class.
	 *
	 * @param args command line arguments.
	 *
	 * @throws IOException if something goes wrong while processing the text.
	 */
    public static void main(String[] args) throws IOException {
        final String text = "Hello wtf is going on";
        AnalyzerUtil.consumeTokenStream(new BasicAnalyzer(), text);
    }

}

package de.muenchen.isi.configuration.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

/**
 * https://docs.jboss.org/hibernate/search/6.1/reference/en-US/html_single/#backend-elasticsearch-analysis-analyzers
 */
@NoArgsConstructor
@Data
public class SearchwordAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(final ElasticsearchAnalysisConfigurationContext context) {
        /**
         * Analyzer für Suchwortermittlung
         */
        context
            .analyzer("searchword_analyzer_string_field")
            .custom()
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keyword-tokenizer.html
            // Verwendung Keyword-Tokenizer um Suchwörter im ganzen zu erhalten.
            .tokenizer("keyword")
            .tokenFilters("lowercase_tokenfilter");

        /**
         * Analyzer für Entitätsermittlung auf Basis von gefundenen Suchwörter
         */
        context
            .analyzer("entity_analyzer_string_field")
            .custom()
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-whitespace-tokenizer.html
            .tokenizer("whitespace")
            .tokenFilters("lowercase_tokenfilter", "edge_ngram_tokenfilter");

        context
            .tokenFilter("lowercase_tokenfilter")
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lowercase-tokenfilter.html
            .type("lowercase");

        context
            .tokenFilter("edge_ngram_tokenfilter")
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-edgengram-tokenfilter.html
            .type("edge_ngram")
            .param("min_gram", "1")
            .param("max_gram", "255");
    }
}
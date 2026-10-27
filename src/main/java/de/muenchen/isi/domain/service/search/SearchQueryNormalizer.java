package de.muenchen.isi.domain.service.search;

/**
 * Utility for normalizing user-provided search queries before tokenization and adaptation to
 * simple query string format.
 *
 * NOTE: The normalization performed here must match the index-side analyzer configuration
 * (see de.muenchen.isi.configuration.search.ElasticsearchAnalysisConfig). In particular,
 * if underscores are treated as token separators in the analyzer, the normalizer must split
 * underscores on the query side (or vice versa). Keep both in sync.
 */
public final class SearchQueryNormalizer {

    private SearchQueryNormalizer() {}

    /**
     * Normalize the input string for tokenization:
     * - convert null to empty string
     * - replace underscores with spaces so they are treated as separators
     * - trim leading/trailing whitespace
     *
     * This method intentionally does not lowercase or otherwise alter characters; case handling
     * should be decided and documented separately (analyzer vs. query normalization).
     *
     * @param input raw user input
     * @return normalized string ready for tokenization
     */
    public static String normalizeForTokenization(final String input) {
        if (input == null) {
            return "";
        }
        return input.replace('_', ' ').trim();
    }
}

package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class SearchQueryNormalizerTest {

    @Test
    void normalizeNullReturnsEmpty() {
        var result = SearchQueryNormalizer.normalizeForTokenization(null);
        assertThat(result, is(""));
    }

    @Test
    void normalizeReplacesUnderscoresWithSpacesAndTrims() {
        var result = SearchQueryNormalizer.normalizeForTokenization("  _test_fall  ");
        assertThat(result, is("test fall"));
    }

    @Test
    void normalizeHandlesMultipleUnderscores() {
        var result = SearchQueryNormalizer.normalizeForTokenization("a__b_c");
        assertThat(result, is("a  b c"));
    }

    @Test
    void normalizeLeavesOtherCharactersUntouched() {
        var result = SearchQueryNormalizer.normalizeForTokenization("TeSt-123 ÄÖÜ");
        assertThat(result, is("TeSt-123 ÄÖÜ"));
    }
}

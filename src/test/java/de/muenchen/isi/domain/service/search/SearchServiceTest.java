package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SearchServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private SearchPreparationService searchPreparationService;

    private final SearchService searchService = new SearchService(entityManager, searchPreparationService, null);

    @Test
    void createAdaptedSearchQuery() {
        var searchQuery =
            " test test1 te--st2 \"in Anfuehrungszeichen\" test2       \"xxx xxx      1234 y\"   27.07.1982   test500?! ";
        assertThat(
            searchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery),
            is("test* test1* te--st2* \"in Anfuehrungszeichen\" test2* \"xxx xxx      1234 y\" 27.07.1982* test500?!*")
        );
        searchQuery = "test500?!";
        assertThat(searchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery), is("test500?!*"));
        searchQuery = "\"in Anfuehrungszeichen\"";
        assertThat(
            searchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery),
            is("\"in Anfuehrungszeichen\"")
        );
    }
}

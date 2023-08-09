package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EntitySearchServiceTest {

    @Mock
    private EntityManager entityManager;

    private SearchPreparationService searchPreparationService = new SearchPreparationService();

    @Mock
    private SearchDomainMapper searchDomainMapper;

    private EntitySearchService entitySearchService = new EntitySearchService(
        entityManager,
        searchPreparationService,
        searchDomainMapper
    );

    @Test
    void createAdaptedSearchQueryForSimpleQueryStringSearch() {
        var searchQuery = "  test-abc123 ?!\"dddds      abf1-test     \"testinger\"    ";
        var result = entitySearchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);
        assertThat(result, is("test-abc123* ?!\"dddds* abf1-test* \"testinger\"*"));

        searchQuery = "     ";
        result = entitySearchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);
        assertThat(result, is(""));

        searchQuery = "";
        result = entitySearchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);
        assertThat(result, is(""));

        searchQuery = null;
        result = entitySearchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);
        assertThat(result, is(""));
    }
}

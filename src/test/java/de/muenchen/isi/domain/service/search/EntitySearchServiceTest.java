package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EntitySearchServiceTest {

    private SearchPreparationService searchPreparationService = new SearchPreparationService();

    @Mock
    private SearchDomainMapper searchDomainMapper;

    private EntitySearchService entitySearchService = new EntitySearchService(
        searchPreparationService,
        searchDomainMapper
    );

    @Test
    void createAdaptedSearchQueryForSimpleQueryStringSearch() {
        var searchQuery = "  test-abc123 ?!\"dddds      abf1-test     \"testinger\"    ";
        var result = entitySearchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);
        assertThat(result, is("test* abc123* dddds* abf1* test* testinger*"));

        searchQuery = "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.";
        result = entitySearchService.createAdaptedSearchQueryForSimpleQueryStringSearch(searchQuery);
        assertThat(result, is("The* 2* QUICK* Brown* Foxes* jumped* over* the* lazy* dog's* bone*"));

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

    @Test
    void tokenizeAccordingUnicodeAnnex29() {
        var searchQuery = "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.";
        var result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(
            result,
            is(new String[] { "The", "2", "QUICK", "Brown", "Foxes", "jumped", "over", "the", "lazy", "dog's", "bone" })
        );

        searchQuery = "      ";
        result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(result, is(new String[] {}));

        searchQuery = "";
        result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(result, is(new String[] {}));

        searchQuery = "., -   , :    .  .";
        result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(result, is(new String[] {}));
    }

    @Test
    void calculateOffsetOrNullIfNoPaginationRequired() {
        var searchQueryAndSortingModel = new SearchQueryAndSortingModel();
        searchQueryAndSortingModel.setPage(null);
        searchQueryAndSortingModel.setPageSize(null);
        var result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(nullValue()));

        searchQueryAndSortingModel = new SearchQueryAndSortingModel();
        searchQueryAndSortingModel.setPage(100);
        searchQueryAndSortingModel.setPageSize(null);
        result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(nullValue()));

        searchQueryAndSortingModel = new SearchQueryAndSortingModel();
        searchQueryAndSortingModel.setPage(null);
        searchQueryAndSortingModel.setPageSize(100);
        result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(nullValue()));

        searchQueryAndSortingModel.setPage(1);
        searchQueryAndSortingModel.setPageSize(20);
        result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(0));

        searchQueryAndSortingModel.setPage(1);
        searchQueryAndSortingModel.setPageSize(20);
        result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(0));

        searchQueryAndSortingModel.setPage(2);
        searchQueryAndSortingModel.setPageSize(20);
        result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(20));

        searchQueryAndSortingModel.setPage(3);
        searchQueryAndSortingModel.setPageSize(20);
        result = entitySearchService.calculateOffsetOrNullIfNoPaginationRequired(searchQueryAndSortingModel);
        assertThat(result, is(40));
    }

    @Test
    void calculateNumberOfPages() {
        var result = entitySearchService.calculateNumberOfPages(100, 20);
        assertThat(result, is(5L));

        result = entitySearchService.calculateNumberOfPages(101, 20);
        assertThat(result, is(6L));

        result = entitySearchService.calculateNumberOfPages(99, 20);
        assertThat(result, is(5L));

        result = entitySearchService.calculateNumberOfPages(1, 20);
        assertThat(result, is(1L));

        result = entitySearchService.calculateNumberOfPages(20, 20);
        assertThat(result, is(1L));

        result = entitySearchService.calculateNumberOfPages(0, 20);
        assertThat(result, is(0L));
    }
}

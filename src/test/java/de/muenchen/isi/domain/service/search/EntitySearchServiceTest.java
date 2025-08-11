package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.model.search.request.AbfrageRecord;
import de.muenchen.isi.domain.model.search.request.BauvorhabenRecord;
import de.muenchen.isi.domain.model.search.request.CompositeEntityProjection;
import de.muenchen.isi.domain.model.search.request.InfrastrukturRecord;
import de.muenchen.isi.domain.model.search.request.SearchQueryAndSortingModel;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EntitySearchServiceTest {

    private final SearchPreparationService searchPreparationService = new SearchPreparationService();

    @Mock
    private SearchDomainMapper searchDomainMapper;

    @Mock
    private AuthenticationUtils authenticationUtils;

    private final EntitySearchService entitySearchService = new EntitySearchService(
        searchPreparationService,
        searchDomainMapper,
        authenticationUtils
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
        var searchQuery = "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone. | and this is a \"test\"";
        var result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(
            result,
            is(
                List.of(
                    "The",
                    "2",
                    "QUICK",
                    "Brown",
                    "Foxes",
                    "jumped",
                    "over",
                    "the",
                    "lazy",
                    "dog's",
                    "bone",
                    "and",
                    "this",
                    "is",
                    "a",
                    "test"
                )
            )
        );

        searchQuery = "      ";
        result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(result, is(List.of()));

        searchQuery = "";
        result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(result, is(List.of()));

        searchQuery = "., -   , :    .  .";
        result = entitySearchService.tokenizeAccordingUnicodeAnnex29(searchQuery);
        assertThat(result, is(List.of()));
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

    @Test
    void test_determineRecordClass() {
        var s1 = new SearchQueryAndSortingModel();
        s1.setSelectBauleitplanverfahren(true);
        s1.setSelectBaugenehmigungsverfahren(true);
        s1.setSelectWeiteresVerfahren(true);
        s1.setSelectBauvorhaben(false);
        s1.setSelectGrundschule(false);
        s1.setSelectGsNachmittagBetreuung(false);
        s1.setSelectHausFuerKinder(false);
        s1.setSelectKindergarten(false);
        s1.setSelectKinderkrippe(false);
        s1.setSelectMittelschule(false);
        Class<?> r1 = entitySearchService.determineRecordClass(s1);
        assertThat(r1, is(AbfrageRecord.class));

        var s2 = new SearchQueryAndSortingModel();
        s2.setSelectBauleitplanverfahren(false);
        s2.setSelectBaugenehmigungsverfahren(false);
        s2.setSelectWeiteresVerfahren(false);
        s2.setSelectBauvorhaben(true);
        s2.setSelectGrundschule(false);
        s2.setSelectGsNachmittagBetreuung(false);
        s2.setSelectHausFuerKinder(false);
        s2.setSelectKindergarten(false);
        s2.setSelectKinderkrippe(false);
        s2.setSelectMittelschule(false);
        Class<?> r2 = entitySearchService.determineRecordClass(s2);
        assertThat(r2, is(BauvorhabenRecord.class));

        var s3 = new SearchQueryAndSortingModel();
        s3.setSelectBauleitplanverfahren(false);
        s3.setSelectBaugenehmigungsverfahren(false);
        s3.setSelectWeiteresVerfahren(false);
        s3.setSelectBauvorhaben(false);
        s3.setSelectGrundschule(true);
        s3.setSelectGsNachmittagBetreuung(true);
        s3.setSelectHausFuerKinder(true);
        s3.setSelectKindergarten(true);
        s3.setSelectKinderkrippe(true);
        s3.setSelectMittelschule(true);
        Class<?> r3 = entitySearchService.determineRecordClass(s3);
        assertThat(r3, is(InfrastrukturRecord.class));

        var s4 = new SearchQueryAndSortingModel();
        s4.setSelectBauleitplanverfahren(false);
        s4.setSelectBaugenehmigungsverfahren(false);
        s4.setSelectWeiteresVerfahren(false);
        s4.setSelectBauvorhaben(true);
        s4.setSelectGrundschule(true);
        s4.setSelectGsNachmittagBetreuung(false);
        s4.setSelectHausFuerKinder(false);
        s4.setSelectKindergarten(false);
        s4.setSelectKinderkrippe(false);
        s4.setSelectMittelschule(false);
        Class<?> r4 = entitySearchService.determineRecordClass(s4);
        assertThat(r4, is(CompositeEntityProjection.class));

        var s5 = new SearchQueryAndSortingModel();
        s5.setSelectBauleitplanverfahren(false);
        s5.setSelectBaugenehmigungsverfahren(false);
        s5.setSelectWeiteresVerfahren(false);
        s5.setSelectBauvorhaben(false);
        s5.setSelectGrundschule(false);
        s5.setSelectGsNachmittagBetreuung(false);
        s5.setSelectHausFuerKinder(false);
        s5.setSelectKindergarten(false);
        s5.setSelectKinderkrippe(false);
        s5.setSelectMittelschule(false);
        Class<?> r5 = entitySearchService.determineRecordClass(s5);
        assertThat(r5, is(CompositeEntityProjection.class));
    }
}

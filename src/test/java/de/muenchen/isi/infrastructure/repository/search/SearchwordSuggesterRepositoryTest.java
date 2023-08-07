package de.muenchen.isi.infrastructure.repository.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SearchwordSuggesterRepositoryTest {

    private SearchwordSuggesterRepository searchwordSuggesterRepository = new SearchwordSuggesterRepository(null);

    @Test
    void getSearchableIndex() {
        var result = searchwordSuggesterRepository.getSearchableIndex(Infrastrukturabfrage.class);
        assertThat(result, is("infrastrukturabfrage-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(Bauvorhaben.class);
        assertThat(result, is("bauvorhaben-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(Grundschule.class);
        assertThat(result, is("grundschule-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(GsNachmittagBetreuung.class);
        assertThat(result, is("gsnachmittagbetreuung-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(HausFuerKinder.class);
        assertThat(result, is("hausfuerkinder-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(Kindergarten.class);
        assertThat(result, is("kindergarten-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(Kinderkrippe.class);
        assertThat(result, is("kinderkrippe-read"));
        result = searchwordSuggesterRepository.getSearchableIndex(Mittelschule.class);
        assertThat(result, is("mittelschule-read"));
    }
}

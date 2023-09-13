package de.muenchen.isi.infrastructure.repository.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SearchwordSuggesterRepositoryTest {

    private SearchwordSuggesterRepository searchwordSuggesterRepository = new SearchwordSuggesterRepository();

    @Test
    void createMultisearchResponseRequestBody() throws JsonProcessingException {
        final Map<Class<? extends BaseEntity>, Set<String>> attributesForSearchableEntities = new HashMap<>();
        attributesForSearchableEntities.put(
            Bauvorhaben.class,
            Set.of("attribute1", "attribut2.subattribut1", "attribut3")
        );
        attributesForSearchableEntities.put(Grundschule.class, Set.of("attribut5.subattribut1"));
        attributesForSearchableEntities.put(Mittelschule.class, Set.of("attribute6"));
        final var result = searchwordSuggesterRepository
            .createMultisearchResponseRequestBody(attributesForSearchableEntities, "die-query")
            .toMultiSearchRequestBody();

        final var expected =
            "{\"index\":\"bauvorhaben-read\"}\n{\"_source\":\"unknown\",\"suggest\":{\"attribute1\":{\"text\":\"die-query\",\"completion\":{\"field\":\"attribute1\",\"size\":5,\"fuzzy\":{\"fuzziness\":\"AUTO\"}}},\"attribut2.subattribut1\":{\"text\":\"die-query\",\"completion\":{\"field\":\"attribut2.subattribut1\",\"size\":5,\"fuzzy\":{\"fuzziness\":\"AUTO\"}}},\"attribut3\":{\"text\":\"die-query\",\"completion\":{\"field\":\"attribut3\",\"size\":5,\"fuzzy\":{\"fuzziness\":\"AUTO\"}}}}}\n{\"index\":\"mittelschule-read\"}\n{\"_source\":\"unknown\",\"suggest\":{\"attribute6\":{\"text\":\"die-query\",\"completion\":{\"field\":\"attribute6\",\"size\":5,\"fuzzy\":{\"fuzziness\":\"AUTO\"}}}}}\n{\"index\":\"grundschule-read\"}\n{\"_source\":\"unknown\",\"suggest\":{\"attribut5.subattribut1\":{\"text\":\"die-query\",\"completion\":{\"field\":\"attribut5.subattribut1\",\"size\":5,\"fuzzy\":{\"fuzziness\":\"AUTO\"}}}}}\n";

        assertThat(result, is(expected));
    }

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

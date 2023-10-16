package de.muenchen.isi.domain.service.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.search.request.SearchQueryModel;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SearchPreparationServiceTest {

    private SearchPreparationService searchPreparationService = new SearchPreparationService();

    @Test
    void getNamesOfSearchableAttributesForSearchwordSuggestion() {
        var result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(
            Bauleitplanverfahren.class
        );
        var expected = new HashSet<String>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("verortung.stadtbezirke.name_searchword_suggestion");
        expected.add("verortung.gemarkungen.name_searchword_suggestion");
        expected.add("verortung.gemarkungen.flurstuecke.nummer_searchword_suggestion");
        expected.add("statusAbfrage_searchword_suggestion");
        expected.add("standVerfahren_searchword_suggestion");
        expected.add("bebauungsplannummer_searchword_suggestion");
        expected.add("name_searchword_suggestion");
        expected.add("abfragevarianten.realisierungVon_searchword_suggestion");
        expected.add("abfragevariantenSachbearbeitung.realisierungVon_searchword_suggestion");
        assertThat(result, is(expected));

        result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(Bauvorhaben.class);
        expected = new HashSet<>();
        expected.add("nameVorhaben_searchword_suggestion");
        expected.add("standVerfahren_searchword_suggestion");
        expected.add("bauvorhabenNummer_searchword_suggestion");
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("verortung.stadtbezirke.name_searchword_suggestion");
        expected.add("verortung.gemarkungen.name_searchword_suggestion");
        expected.add("verortung.gemarkungen.flurstuecke.nummer_searchword_suggestion");
        expected.add("bebauungsplannummer_searchword_suggestion");
        assertThat(result, is(expected));

        result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(Grundschule.class);
        expected = new HashSet<>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("nameEinrichtung_searchword_suggestion");
        expected.add("status_searchword_suggestion");
        assertThat(result, is(expected));

        result =
            searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(GsNachmittagBetreuung.class);
        expected = new HashSet<>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("nameEinrichtung_searchword_suggestion");
        expected.add("status_searchword_suggestion");
        assertThat(result, is(expected));

        result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(HausFuerKinder.class);
        expected = new HashSet<>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("nameEinrichtung_searchword_suggestion");
        expected.add("status_searchword_suggestion");
        assertThat(result, is(expected));

        result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(Kindergarten.class);
        expected = new HashSet<>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("nameEinrichtung_searchword_suggestion");
        expected.add("status_searchword_suggestion");
        assertThat(result, is(expected));

        result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(Kinderkrippe.class);
        expected = new HashSet<>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("nameEinrichtung_searchword_suggestion");
        expected.add("status_searchword_suggestion");
        assertThat(result, is(expected));

        result = searchPreparationService.getNamesOfSearchableAttributesForSearchwordSuggestion(Mittelschule.class);
        expected = new HashSet<>();
        expected.add("adresse.strasse_searchword_suggestion");
        expected.add("adresse.hausnummer_searchword_suggestion");
        expected.add("nameEinrichtung_searchword_suggestion");
        expected.add("status_searchword_suggestion");
        assertThat(result, is(expected));
    }

    @Test
    void getNamesOfSearchableAttributes() {
        var result = searchPreparationService.getNamesOfSearchableAttributes(List.of(Bauleitplanverfahren.class));
        var expected = new HashSet<String>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("verortung.stadtbezirke.name");
        expected.add("verortung.gemarkungen.name");
        expected.add("verortung.gemarkungen.flurstuecke.nummer");
        expected.add("statusAbfrage");
        expected.add("standVerfahren");
        expected.add("bebauungsplannummer");
        expected.add("name");
        expected.add("abfragevarianten.realisierungVon");
        expected.add("abfragevariantenSachbearbeitung.realisierungVon");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(Bauvorhaben.class));
        expected = new HashSet<>();
        expected.add("nameVorhaben");
        expected.add("standVerfahren");
        expected.add("bauvorhabenNummer");
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("verortung.stadtbezirke.name");
        expected.add("verortung.gemarkungen.name");
        expected.add("verortung.gemarkungen.flurstuecke.nummer");
        expected.add("bebauungsplannummer");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(Grundschule.class));
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(GsNachmittagBetreuung.class));
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(HausFuerKinder.class));
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(Kindergarten.class));
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(Kinderkrippe.class));
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));

        result = searchPreparationService.getNamesOfSearchableAttributes(List.of(Mittelschule.class));
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));

        result =
            searchPreparationService.getNamesOfSearchableAttributes(
                List.of(
                    Bauleitplanverfahren.class,
                    Bauvorhaben.class,
                    Grundschule.class,
                    GsNachmittagBetreuung.class,
                    HausFuerKinder.class,
                    Kindergarten.class,
                    Kinderkrippe.class,
                    Mittelschule.class
                )
            );
        expected = new HashSet<>();
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("verortung.stadtbezirke.name");
        expected.add("verortung.gemarkungen.name");
        expected.add("verortung.gemarkungen.flurstuecke.nummer");
        expected.add("statusAbfrage");
        expected.add("standVerfahren");
        expected.add("bebauungsplannummer");
        expected.add("name");
        expected.add("abfragevarianten.realisierungVon");
        expected.add("abfragevariantenSachbearbeitung.realisierungVon");
        expected.add("nameVorhaben");
        expected.add("standVerfahren");
        expected.add("bauvorhabenNummer");
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("verortung.stadtbezirke.name");
        expected.add("verortung.gemarkungen.name");
        expected.add("verortung.gemarkungen.flurstuecke.nummer");
        expected.add("bebauungsplannummer");
        expected.add("adresse.strasse");
        expected.add("adresse.hausnummer");
        expected.add("nameEinrichtung");
        expected.add("status");
        assertThat(result, is(expected.toArray(String[]::new)));
    }

    @Test
    void getSearchableEntities() throws EntityNotFoundException {
        final var searchQueryModel = new SearchQueryModel();
        searchQueryModel.setSelectBauleitplanverfahren(true);
        searchQueryModel.setSelectBauvorhaben(true);
        searchQueryModel.setSelectGrundschule(true);
        searchQueryModel.setSelectGsNachmittagBetreuung(true);
        searchQueryModel.setSelectHausFuerKinder(true);
        searchQueryModel.setSelectKindergarten(true);
        searchQueryModel.setSelectKinderkrippe(true);
        searchQueryModel.setSelectMittelschule(true);
        var result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(
            result,
            is(
                List.of(
                    Bauleitplanverfahren.class,
                    Bauvorhaben.class,
                    Grundschule.class,
                    GsNachmittagBetreuung.class,
                    HausFuerKinder.class,
                    Kindergarten.class,
                    Kinderkrippe.class,
                    Mittelschule.class
                )
            )
        );

        searchQueryModel.setSelectBauleitplanverfahren(true);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Bauleitplanverfahren.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(true);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Bauvorhaben.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(true);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Grundschule.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(true);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(GsNachmittagBetreuung.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(true);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(HausFuerKinder.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(true);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Kindergarten.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(true);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Kinderkrippe.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(true);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Mittelschule.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(true);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(true);
        searchQueryModel.setSelectMittelschule(false);
        result = searchPreparationService.getSearchableEntities(searchQueryModel);
        assertThat(result, is(List.of(Bauvorhaben.class, Kinderkrippe.class)));

        searchQueryModel.setSelectBauleitplanverfahren(false);
        searchQueryModel.setSelectBauvorhaben(false);
        searchQueryModel.setSelectGrundschule(false);
        searchQueryModel.setSelectGsNachmittagBetreuung(false);
        searchQueryModel.setSelectHausFuerKinder(false);
        searchQueryModel.setSelectKindergarten(false);
        searchQueryModel.setSelectKinderkrippe(false);
        searchQueryModel.setSelectMittelschule(false);
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.searchPreparationService.getSearchableEntities(searchQueryModel)
        );
    }
}

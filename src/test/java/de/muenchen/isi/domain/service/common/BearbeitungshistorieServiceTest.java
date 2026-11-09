package de.muenchen.isi.domain.service.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.BearbeitungsArt;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
class BearbeitungshistorieServiceTest {

    @Autowired
    private BearbeitungshistorieService bearbeitungshistorieService;

    @Test
    @MockCustomUser
    void appendBearbeitungshistorieToAbfrage() {
        final var zeitpunkt = LocalDateTime.of(2024, 3, 14, 14, 1, 1, 1);

        final var abfrageToSave = new BauleitplanverfahrenModel();
        abfrageToSave.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        AbfrageModel result;
        try (final MockedStatic<LocalDateTime> mockLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockLocalDateTime.when(LocalDateTime::now).thenReturn(zeitpunkt);
            result = bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(
                abfrageToSave,
                BearbeitungsArt.ERSTELLT
            );
        }

        final var expected = new BauleitplanverfahrenModel();
        expected.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        final var bearbeitungshistorieExpected = new BearbeitungshistorieModel();
        bearbeitungshistorieExpected.setArt(BearbeitungsArt.ERSTELLT);
        bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorieExpected.setZeitpunkt(zeitpunkt);
        final var bearbeitendePersonExpected = new BearbeitendePersonModel();
        bearbeitendePersonExpected.setName("Rob Winch");
        bearbeitendePersonExpected.setEmail("rob@example.com");
        bearbeitendePersonExpected.setOrganisationseinheit("IT");
        bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);
        expected.getBearbeitungshistorie().add(bearbeitungshistorieExpected);

        assertThat(result, is(expected));
    }

    @Test
    @MockCustomUser
    void createBearbeitungshistorieForStatus() {
        final var zeitpunkt = LocalDateTime.of(2024, 3, 14, 14, 1, 1, 1);
        BearbeitungshistorieModel result;

        try (final MockedStatic<LocalDateTime> mockLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockLocalDateTime.when(LocalDateTime::now).thenReturn(zeitpunkt);
            result = bearbeitungshistorieService.createBearbeitungshistorieForStatus(
                StatusAbfrage.EINPLANUNG_BEDARFE,
                BearbeitungsArt.STATUSAENDERUNG
            );
        }

        final var bearbeitungshistorieExpected = new BearbeitungshistorieModel();
        bearbeitungshistorieExpected.setArt(BearbeitungsArt.STATUSAENDERUNG);
        bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.EINPLANUNG_BEDARFE);
        bearbeitungshistorieExpected.setZeitpunkt(zeitpunkt);
        final var bearbeitendePersonExpected = new BearbeitendePersonModel();
        bearbeitendePersonExpected.setName("Rob Winch");
        bearbeitendePersonExpected.setEmail("rob@example.com");
        bearbeitendePersonExpected.setOrganisationseinheit("IT");
        bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);

        assertThat(result, is(bearbeitungshistorieExpected));
    }

    @Test
    @MockCustomUser
    void appendBearbeitungshistorieToAbfrageAktualisierungOhneZielstatus() {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);

        bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage, BearbeitungsArt.AKTUALISIERT);

        final var eintrag = abfrage.getBearbeitungshistorie().get(0);
        assertThat(eintrag.getArt(), is(BearbeitungsArt.AKTUALISIERT));
        assertThat(eintrag.getZielStatus(), is(nullValue()));
        assertThat(eintrag.getBearbeitendePerson().getEmail(), is("rob@example.com"));
    }

    @Test
    @MockCustomUser
    void fortschreibenBeiNeuanlage() {
        final var result = bearbeitungshistorieService.fortschreiben(null, id -> {
            throw new AssertionError("Bei einer Neuanlage gibt es keine gespeicherte Historie.");
        });

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getArt(), is(BearbeitungsArt.ERSTELLT));
        assertThat(result.get(0).getZielStatus(), is(nullValue()));
        assertThat(result.get(0).getBearbeitendePerson().getEmail(), is("rob@example.com"));
    }

    @Test
    @MockCustomUser
    void fortschreibenBeiAktualisierungBehaeltBisherigeEintraege() {
        final var id = UUID.randomUUID();
        final var bisher = new Bearbeitungshistorie();
        bisher.setArt(BearbeitungsArt.ERSTELLT);

        final var result = bearbeitungshistorieService.fortschreiben(id, gesuchteId -> {
            assertThat(gesuchteId, is(id));
            return List.of(bisher);
        });

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(bisher));
        assertThat(result.get(1).getArt(), is(BearbeitungsArt.AKTUALISIERT));
    }

    @Test
    @MockCustomUser
    void fortschreibenErsetztLetzteAktualisierungDerselbenPerson() {
        final var erstellt = eintrag(BearbeitungsArt.ERSTELLT, "rob@example.com");
        final var aktualisiert = eintrag(BearbeitungsArt.AKTUALISIERT, "ROB@example.com");
        aktualisiert.setZeitpunkt(LocalDateTime.of(2024, 3, 14, 14, 1));

        final var result = bearbeitungshistorieService.fortschreiben(UUID.randomUUID(), id ->
            List.of(erstellt, aktualisiert)
        );

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(erstellt));
        assertThat(result.get(1).getArt(), is(BearbeitungsArt.AKTUALISIERT));
        assertThat(result.get(1).getBearbeitendePerson().getName(), is("Rob Winch"));
        assertThat(result.get(1).getZeitpunkt().isAfter(aktualisiert.getZeitpunkt()), is(true));
    }

    @Test
    @MockCustomUser
    void fortschreibenErgaenztAktualisierungEinerAnderenPerson() {
        final var aktualisiert = eintrag(BearbeitungsArt.AKTUALISIERT, "andere@example.com");

        final var result = bearbeitungshistorieService.fortschreiben(UUID.randomUUID(), id -> List.of(aktualisiert));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(aktualisiert));
        assertThat(result.get(1).getBearbeitendePerson().getEmail(), is("rob@example.com"));
    }

    @Test
    @MockCustomUser
    void fortschreibenErgaenztBeiAktualisierungOhnePerson() {
        final var ohnePerson = new Bearbeitungshistorie();
        ohnePerson.setArt(BearbeitungsArt.AKTUALISIERT);

        final var result = bearbeitungshistorieService.fortschreiben(UUID.randomUUID(), id -> List.of(ohnePerson));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(ohnePerson));
    }

    @Test
    @MockCustomUser
    void fortschreibenErsetztErstellungNicht() {
        final var erstellt = eintrag(BearbeitungsArt.ERSTELLT, "rob@example.com");

        final var result = bearbeitungshistorieService.fortschreiben(UUID.randomUUID(), id -> List.of(erstellt));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(erstellt));
    }

    @Test
    @MockCustomUser
    void appendBearbeitungshistorieToAbfrageFasstAktualisierungenZusammen() {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);

        bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage, BearbeitungsArt.AKTUALISIERT);
        bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage, BearbeitungsArt.AKTUALISIERT);

        assertThat(abfrage.getBearbeitungshistorie(), hasSize(1));
    }

    @Test
    @MockCustomUser
    void appendBearbeitungshistorieToAbfrageStatusaenderungTrenntAktualisierungen() {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);

        bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage, BearbeitungsArt.AKTUALISIERT);
        bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage, BearbeitungsArt.STATUSAENDERUNG);
        bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrage, BearbeitungsArt.AKTUALISIERT);

        assertThat(
            abfrage.getBearbeitungshistorie().stream().map(BearbeitungshistorieModel::getArt).toList(),
            is(List.of(BearbeitungsArt.AKTUALISIERT, BearbeitungsArt.STATUSAENDERUNG, BearbeitungsArt.AKTUALISIERT))
        );
    }

    private static Bearbeitungshistorie eintrag(final BearbeitungsArt art, final String email) {
        final var person = new BearbeitendePerson();
        person.setEmail(email);
        final var eintrag = new Bearbeitungshistorie();
        eintrag.setArt(art);
        eintrag.setBearbeitendePerson(person);
        return eintrag;
    }
}

package de.muenchen.isi.infrastructure.adapter.listener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
class AbfrageListenerTest {

    @Autowired
    private AbfrageListener abfrageListener;

    @MockBean
    private AbfrageRepository abfrageRepository;

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(abfrageRepository);
    }

    @Test
    @MockCustomUser
    void beforeSaveAbfrageIdNull() {
        final var localDateTime = LocalDateTime.of(2024, 3, 14, 14, 1, 1, 1);

        try (final MockedStatic<LocalDateTime> mockLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            final var abfrageToSave = new Bauleitplanverfahren();
            abfrageToSave.setStatusAbfrage(StatusAbfrage.ANGELEGT);

            mockLocalDateTime.when(LocalDateTime::now).thenReturn(localDateTime);

            abfrageListener.beforeSave(abfrageToSave);

            final var expected = new Bauleitplanverfahren();
            expected.setStatusAbfrage(StatusAbfrage.ANGELEGT);
            final var bearbeitungshistorieExpected = new Bearbeitungshistorie();
            bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.ANGELEGT);
            bearbeitungshistorieExpected.setZeitpunkt(localDateTime);
            final var bearbeitendePersonExpected = new BearbeitendePerson();
            bearbeitendePersonExpected.setName("Rob Winch");
            bearbeitendePersonExpected.setEmail("rob@example.com");
            bearbeitendePersonExpected.setOrganisationseinheit("IT");
            bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);
            expected.getBearbeitungshistorie().add(bearbeitungshistorieExpected);

            assertThat(abfrageToSave, is(expected));

            Mockito.verify(this.abfrageRepository, Mockito.times(0)).findById(Mockito.any());
        }
    }

    @Test
    @MockCustomUser(
        givenname = "Neil",
        surname = "Armstrong",
        department = "NASA John F. Kennedy Space Center",
        email = "neil.armstrong@nasa.gov"
    )
    void beforeSaveAbfrageIdNotNullAndAvailableInRepoWithDifferentState() {
        final var localDateTime1 = LocalDateTime.of(2024, 3, 14, 14, 1, 1, 1);
        final var localDateTime2 = LocalDateTime.of(2024, 3, 15, 15, 2, 2, 2);

        try (final MockedStatic<LocalDateTime> mockLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            final var abfrageToSave = new Bauleitplanverfahren();
            abfrageToSave.setId(UUID.randomUUID());
            abfrageToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
            final var bearbeitungshistorie = new Bearbeitungshistorie();
            bearbeitungshistorie.setZielStatus(StatusAbfrage.ANGELEGT);
            bearbeitungshistorie.setZeitpunkt(localDateTime1);
            final var bearbeitendePerson = new BearbeitendePerson();
            bearbeitendePerson.setName("Rob Winch");
            bearbeitendePerson.setEmail("rob@example.com");
            bearbeitendePerson.setOrganisationseinheit("IT");
            bearbeitungshistorie.setBearbeitendePerson(bearbeitendePerson);
            abfrageToSave.getBearbeitungshistorie().add(bearbeitungshistorie);

            mockLocalDateTime.when(LocalDateTime::now).thenReturn(localDateTime2);

            final var foundAbfrage = new Bauleitplanverfahren();
            foundAbfrage.setId(abfrageToSave.getId());
            foundAbfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
            Mockito.when(this.abfrageRepository.findById(abfrageToSave.getId())).thenReturn(Optional.of(foundAbfrage));

            abfrageListener.beforeSave(abfrageToSave);

            final var expected = new Bauleitplanverfahren();
            expected.setId(abfrageToSave.getId());
            expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);

            var bearbeitungshistorieExpected = new Bearbeitungshistorie();
            bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.ANGELEGT);
            bearbeitungshistorieExpected.setZeitpunkt(localDateTime1);
            var bearbeitendePersonExpected = new BearbeitendePerson();
            bearbeitendePersonExpected.setName("Rob Winch");
            bearbeitendePersonExpected.setEmail("rob@example.com");
            bearbeitendePersonExpected.setOrganisationseinheit("IT");
            bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);
            expected.getBearbeitungshistorie().add(bearbeitungshistorieExpected);

            bearbeitungshistorieExpected = new Bearbeitungshistorie();
            bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
            bearbeitungshistorieExpected.setZeitpunkt(localDateTime2);
            bearbeitendePersonExpected = new BearbeitendePerson();
            bearbeitendePersonExpected.setName("Neil Armstrong");
            bearbeitendePersonExpected.setEmail("neil.armstrong@nasa.gov");
            bearbeitendePersonExpected.setOrganisationseinheit("NASA John F. Kennedy Space Center");
            bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);
            expected.getBearbeitungshistorie().add(bearbeitungshistorieExpected);

            assertThat(abfrageToSave, is(expected));

            Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(abfrageToSave.getId());
        }
    }

    @Test
    @MockCustomUser(
        givenname = "Neil",
        surname = "Armstrong",
        department = "NASA John F. Kennedy Space Center",
        email = "neil.armstrong@nasa.gov"
    )
    void beforeSaveAbfrageIdNotNullAndAvailableInRepoWithSameState() {
        final var localDateTime1 = LocalDateTime.of(2024, 3, 14, 14, 1, 1, 1);

        final var abfrageToSave = new Bauleitplanverfahren();
        abfrageToSave.setId(UUID.randomUUID());
        abfrageToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        final var bearbeitungshistorie = new Bearbeitungshistorie();
        bearbeitungshistorie.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorie.setZeitpunkt(localDateTime1);
        final var bearbeitendePerson = new BearbeitendePerson();
        bearbeitendePerson.setName("Rob Winch");
        bearbeitendePerson.setEmail("rob@example.com");
        bearbeitendePerson.setOrganisationseinheit("IT");
        bearbeitungshistorie.setBearbeitendePerson(bearbeitendePerson);
        abfrageToSave.getBearbeitungshistorie().add(bearbeitungshistorie);

        final var foundAbfrage = new Bauleitplanverfahren();
        foundAbfrage.setId(abfrageToSave.getId());
        foundAbfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        Mockito.when(this.abfrageRepository.findById(abfrageToSave.getId())).thenReturn(Optional.of(foundAbfrage));

        abfrageListener.beforeSave(abfrageToSave);

        final var expected = new Bauleitplanverfahren();
        expected.setId(abfrageToSave.getId());
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);

        var bearbeitungshistorieExpected = new Bearbeitungshistorie();
        bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorieExpected.setZeitpunkt(localDateTime1);
        var bearbeitendePersonExpected = new BearbeitendePerson();
        bearbeitendePersonExpected.setName("Rob Winch");
        bearbeitendePersonExpected.setEmail("rob@example.com");
        bearbeitendePersonExpected.setOrganisationseinheit("IT");
        bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);
        expected.getBearbeitungshistorie().add(bearbeitungshistorieExpected);

        assertThat(abfrageToSave, is(expected));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(abfrageToSave.getId());
    }

    @Test
    @MockCustomUser(
        givenname = "Neil",
        surname = "Armstrong",
        department = "NASA John F. Kennedy Space Center",
        email = "neil.armstrong@nasa.gov"
    )
    void beforeSaveAbfrageIdNotNullAndNotAvailableInRepo() {
        final var localDateTime1 = LocalDateTime.of(2024, 3, 14, 14, 1, 1, 1);

        final var abfrageToSave = new Bauleitplanverfahren();
        abfrageToSave.setId(UUID.randomUUID());
        abfrageToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        final var bearbeitungshistorie = new Bearbeitungshistorie();
        bearbeitungshistorie.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorie.setZeitpunkt(localDateTime1);
        final var bearbeitendePerson = new BearbeitendePerson();
        bearbeitendePerson.setName("Rob Winch");
        bearbeitendePerson.setEmail("rob@example.com");
        bearbeitendePerson.setOrganisationseinheit("IT");
        bearbeitungshistorie.setBearbeitendePerson(bearbeitendePerson);
        abfrageToSave.getBearbeitungshistorie().add(bearbeitungshistorie);

        Mockito.when(this.abfrageRepository.findById(abfrageToSave.getId())).thenReturn(Optional.empty());

        abfrageListener.beforeSave(abfrageToSave);

        final var expected = new Bauleitplanverfahren();
        expected.setId(abfrageToSave.getId());
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);

        var bearbeitungshistorieExpected = new Bearbeitungshistorie();
        bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorieExpected.setZeitpunkt(localDateTime1);
        var bearbeitendePersonExpected = new BearbeitendePerson();
        bearbeitendePersonExpected.setName("Rob Winch");
        bearbeitendePersonExpected.setEmail("rob@example.com");
        bearbeitendePersonExpected.setOrganisationseinheit("IT");
        bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);
        expected.getBearbeitungshistorie().add(bearbeitungshistorieExpected);

        assertThat(abfrageToSave, is(expected));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(abfrageToSave.getId());
    }
}

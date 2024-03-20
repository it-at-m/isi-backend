package de.muenchen.isi.domain.service.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDateTime;
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
            result = bearbeitungshistorieService.appendBearbeitungshistorieToAbfrage(abfrageToSave);
        }

        final var expected = new BauleitplanverfahrenModel();
        expected.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        final var bearbeitungshistorieExpected = new BearbeitungshistorieModel();
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
            result =
                bearbeitungshistorieService.createBearbeitungshistorieForStatus(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        }

        final var bearbeitungshistorieExpected = new BearbeitungshistorieModel();
        bearbeitungshistorieExpected.setZielStatus(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        bearbeitungshistorieExpected.setZeitpunkt(zeitpunkt);
        final var bearbeitendePersonExpected = new BearbeitendePersonModel();
        bearbeitendePersonExpected.setName("Rob Winch");
        bearbeitendePersonExpected.setEmail("rob@example.com");
        bearbeitendePersonExpected.setOrganisationseinheit("IT");
        bearbeitungshistorieExpected.setBearbeitendePerson(bearbeitendePersonExpected);

        assertThat(result, is(bearbeitungshistorieExpected));
    }
}

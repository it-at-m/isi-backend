package de.muenchen.isi.domain.service.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SendWorkAssignmentInformationServiceTest {

    @Mock
    private MailSenderRepository mailSenderRepository;

    @Mock
    private Environment environment;

    private SendWorkAssignmentInformationService sendWorkAssignmentInformationService;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        this.sendWorkAssignmentInformationService =
            new SendWorkAssignmentInformationService(
                "mailadress-receiver-sachbearbeitung",
                "mailadress-receiver-bedarfsmeldung",
                mailSenderRepository,
                environment
            );
        Mockito.reset(mailSenderRepository, environment);
    }

    @Test
    void sendWorkAssignmentInformationAsync() {
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationText(),
                    ""
                )
            )
            .thenReturn("Der Text ");
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationSubject(),
                    ""
                )
            )
            .thenReturn("Der Betreff ");

        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setName("Name der Abfrage");
        abfrage.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var subject = "Der Betreff Name der Abfrage";
        final var text = "Der Text Name der Abfrage\n\nStadtbezirke: ";
        sendWorkAssignmentInformationService.sendWorkAssignmentInformationAsync(
            abfrage,
            StatusAbfrageEvents.ERNEUTE_BEARBEITUNG
        );

        Mockito
            .verify(mailSenderRepository, Mockito.times(1))
            .sendMail(List.of("mailadress-receiver-sachbearbeitung"), subject, text);
    }

    @Test
    void sendWorkAssignmentInformation() {
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationText(),
                    ""
                )
            )
            .thenReturn("Der Text ");
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationSubject(),
                    ""
                )
            )
            .thenReturn("Der Betreff ");

        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setName("Name der Abfrage");
        abfrage.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var subject = "Der Betreff Name der Abfrage";
        final var text = "Der Text Name der Abfrage\n\nStadtbezirke: ";
        sendWorkAssignmentInformationService.sendWorkAssignmentInformation(
            abfrage,
            StatusAbfrageEvents.ERNEUTE_BEARBEITUNG
        );

        Mockito
            .verify(mailSenderRepository, Mockito.times(1))
            .sendMail(List.of("mailadress-receiver-sachbearbeitung"), subject, text);
    }

    @Test
    void sendWorkAssignmentInformationWithStadtbezirk() {
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationText(),
                    ""
                )
            )
            .thenReturn("Der Text ");
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationSubject(),
                    ""
                )
            )
            .thenReturn("Der Betreff ");

        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setName("Name der Abfrage");
        abfrage.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);

        StadtbezirkModel stadtbezirk = new StadtbezirkModel();
        stadtbezirk.setNummer("99");
        stadtbezirk.setName("TestStadtbezirk");
        Set<StadtbezirkModel> stadtbezirkSet = new HashSet<>();
        stadtbezirkSet.add(stadtbezirk);

        VerortungMultiPolygonModel verortung = new VerortungMultiPolygonModel();
        verortung.setStadtbezirke(stadtbezirkSet);
        abfrage.setVerortung(verortung);

        final var subject = "Der Betreff Name der Abfrage";
        final var text = "Der Text Name der Abfrage\n\nStadtbezirke: 99/TestStadtbezirk";
        sendWorkAssignmentInformationService.sendWorkAssignmentInformation(
            abfrage,
            StatusAbfrageEvents.ERNEUTE_BEARBEITUNG
        );

        Mockito
            .verify(mailSenderRepository, Mockito.times(1))
            .sendMail(List.of("mailadress-receiver-sachbearbeitung"), subject, text);
    }

    @Test
    void getReceiverFreigabe() {
        var result = sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.FREIGABE);
        assertThat(result, is(List.of("mailadress-receiver-sachbearbeitung")));
    }

    @Test
    void getReceiverErneuteBearbeitung() {
        var result = sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
        assertThat(result, is(List.of("mailadress-receiver-sachbearbeitung")));
    }

    @Test
    void getReceiverVerschickenDerStellungsnahme() {
        var result = sendWorkAssignmentInformationService.getReceiver(
            null,
            StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME
        );
        assertThat(result, is(List.of("mailadress-receiver-bedarfsmeldung")));
    }

    @Test
    void getReceiverEinplanungBedarfee() {
        final var abfrage = new BauleitplanverfahrenModel();

        var bearbeitungshistorie = new ArrayList<BearbeitungshistorieModel>();
        var bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-angelegt");
        var historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorie.add(historyElement);

        bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-uebermittelt-zur-bearbeitung");
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);

        bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-start-bearbeitung");
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.START_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);

        abfrage.setBearbeitungshistorie(bearbeitungshistorie);

        var result = sendWorkAssignmentInformationService.getReceiver(
            abfrage,
            StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE
        );
        assertThat(result, is(List.of("the-email-address-uebermittelt-zur-bearbeitung")));
    }

    @Test
    void getReceiverSpeichernVonSozialinfrastrukturVersorgung() {
        var result = sendWorkAssignmentInformationService.getReceiver(
            null,
            StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG
        );
        assertThat(result, is(List.of("mailadress-receiver-sachbearbeitung", "mailadress-receiver-bedarfsmeldung")));
    }

    @Test
    void getReceiverOther() {
        var result = sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.IN_BEARBEITUNG_SETZEN);
        assertThat(result, is(nullValue()));

        result = sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.ABBRECHEN);
        assertThat(result, is(nullValue()));

        result =
            sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.ZURUECK_AN_ABFRAGEERSTELLUNG);
        assertThat(result, is(nullValue()));

        result = sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG);
        assertThat(result, is(nullValue()));

        result = sendWorkAssignmentInformationService.getReceiver(null, StatusAbfrageEvents.ZURUECK_AN_SACHBEARBEITUNG);
        assertThat(result, is(nullValue()));
    }

    @Test
    void getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage() {
        var result = sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(null);
        assertThat(result, is(nullValue()));

        result = sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(List.of());
        assertThat(result, is(nullValue()));

        var bearbeitungshistorie = new ArrayList<BearbeitungshistorieModel>();
        var historyElement = new BearbeitungshistorieModel();
        historyElement.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorie.add(historyElement);
        result =
            sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(
                bearbeitungshistorie
            );
        assertThat(result, is(nullValue()));

        bearbeitungshistorie = new ArrayList<>();
        historyElement = new BearbeitungshistorieModel();
        historyElement.setZielStatus(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);
        result =
            sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(
                bearbeitungshistorie
            );
        assertThat(result, is(nullValue()));

        bearbeitungshistorie = new ArrayList<>();
        var bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail(null);
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);
        result =
            sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(
                bearbeitungshistorie
            );
        assertThat(result, is(nullValue()));

        bearbeitungshistorie = new ArrayList<>();
        bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-uebermittelt-zur-bearbeitung");
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);
        result =
            sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(
                bearbeitungshistorie
            );
        assertThat(result, is("the-email-address-uebermittelt-zur-bearbeitung"));

        bearbeitungshistorie = new ArrayList<>();
        bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-angelegt");
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.ANGELEGT);
        bearbeitungshistorie.add(historyElement);

        bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-uebermittelt-zur-bearbeitung");
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);

        bearbeitendePerson = new BearbeitendePersonModel();
        bearbeitendePerson.setEmail("the-email-address-start-bearbeitung");
        historyElement = new BearbeitungshistorieModel();
        historyElement.setBearbeitendePerson(bearbeitendePerson);
        historyElement.setZielStatus(StatusAbfrage.START_BEARBEITUNG);
        bearbeitungshistorie.add(historyElement);

        result =
            sendWorkAssignmentInformationService.getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(
                bearbeitungshistorie
            );
        assertThat(result, is("the-email-address-uebermittelt-zur-bearbeitung"));
    }

    @Test
    void getText() {
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationText(),
                    ""
                )
            )
            .thenReturn("Der Text ");

        var result = sendWorkAssignmentInformationService.getText(
            "Name der Abfrage",
            StatusAbfrageEvents.ERNEUTE_BEARBEITUNG
        );
        var expected = "Der Text Name der Abfrage";
        assertThat(result, is(expected));

        result = sendWorkAssignmentInformationService.getText("", StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
        expected = "Der Text ";
        assertThat(result, is(expected));

        result = sendWorkAssignmentInformationService.getText(null, StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
        expected = "Der Text ";
        assertThat(result, is(expected));
    }

    @Test
    void getSubject() {
        Mockito
            .when(
                environment.getProperty(
                    StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.getPropertyWorkAssignmentInformationSubject(),
                    ""
                )
            )
            .thenReturn("Der Betreff ");

        var result = sendWorkAssignmentInformationService.getSubject(
            "Name der Abfrage",
            StatusAbfrageEvents.ERNEUTE_BEARBEITUNG
        );
        var expected = "Der Betreff Name der Abfrage";
        assertThat(result, is(expected));

        result = sendWorkAssignmentInformationService.getSubject("", StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
        expected = "Der Betreff ";
        assertThat(result, is(expected));

        result = sendWorkAssignmentInformationService.getSubject(null, StatusAbfrageEvents.ERNEUTE_BEARBEITUNG);
        expected = "Der Betreff ";
        assertThat(result, is(expected));
    }
}

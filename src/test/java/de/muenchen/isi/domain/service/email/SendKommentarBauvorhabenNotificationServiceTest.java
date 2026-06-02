package de.muenchen.isi.domain.service.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SendKommentarBauvorhabenNotificationServiceTest {

    private static final String RECEIVER = "plan.ha1-22-isi@muenchen.de";
    private static final String BASE_URL = "https://isi-dev.test.de";

    @Mock
    private MailSenderRepository mailSenderRepository;

    @Mock
    private AbfrageRepository abfrageRepository;

    private SendKommentarBauvorhabenNotificationService service;
    private SendKommentarBauvorhabenNotificationService serviceOhneUrl;
    private SendKommentarBauvorhabenNotificationService serviceOhneReceiver;

    @BeforeEach
    void beforeEach() {
        service = new SendKommentarBauvorhabenNotificationService(
            RECEIVER,
            BASE_URL,
            mailSenderRepository,
            abfrageRepository
        );
        serviceOhneUrl = new SendKommentarBauvorhabenNotificationService(
            RECEIVER,
            "",
            mailSenderRepository,
            abfrageRepository
        );
        serviceOhneReceiver = new SendKommentarBauvorhabenNotificationService(
            "",
            BASE_URL,
            mailSenderRepository,
            abfrageRepository
        );
        Mockito.reset(mailSenderRepository, abfrageRepository);
    }

    @Test
    void sendKommentarBauvorhabenNotification_versendetEmailMitKorrektemInhalt() {
        final var bauvorhabenId = UUID.randomUUID();
        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.empty());

        service.sendKommentarBauvorhabenNotification(bauvorhabenId, "Mein Bauvorhaben", "Ein Kommentar", "01.06.2026");

        final var expectedSubject = "ISI - Neuer Kommentar zum Bauvorhaben: Mein Bauvorhaben";
        final var expectedText =
            "Im Bauvorhaben wurde ein Kommentar gespeichert." +
            "\n\nBauvorhaben: Mein Bauvorhaben" +
            "\nLink zum Bauvorhaben: " +
            BASE_URL +
            "/#/bauvorhaben/" +
            bauvorhabenId +
            "\n\nDatum des Kommentars: 01.06.2026" +
            "\nText des Kommentars: Ein Kommentar";

        Mockito.verify(mailSenderRepository, Mockito.times(1)).sendMail(
            List.of(RECEIVER),
            expectedSubject,
            expectedText
        );
    }

    @Test
    void sendKommentarBauvorhabenNotification_keinVersandBeiLeeremReceiver() {
        final var bauvorhabenId = UUID.randomUUID();

        serviceOhneReceiver.sendKommentarBauvorhabenNotification(
            bauvorhabenId,
            "Mein Bauvorhaben",
            "Ein Kommentar",
            "01.06.2026"
        );

        Mockito.verify(mailSenderRepository, Mockito.never()).sendMail(
            Mockito.anyList(),
            Mockito.anyString(),
            Mockito.anyString()
        );
    }

    @Test
    void sendKommentarBauvorhabenNotification_mitVerknuepftenAbfragen() {
        final var bauvorhabenId = UUID.randomUUID();
        final var abfrageId1 = UUID.randomUUID();
        final var abfrageId2 = UUID.randomUUID();

        final var abfrage1 = new Baugenehmigungsverfahren();
        abfrage1.setId(abfrageId1);
        abfrage1.setName("Abfrage Alpha");

        final var abfrage2 = new Baugenehmigungsverfahren();
        abfrage2.setId(abfrageId2);
        abfrage2.setName("Abfrage Beta");

        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.of(abfrage1, abfrage2));

        service.sendKommentarBauvorhabenNotification(bauvorhabenId, "Mein Bauvorhaben", "Ein Kommentar", "01.06.2026");

        final var expectedText =
            "Im Bauvorhaben wurde ein Kommentar gespeichert." +
            "\n\nBauvorhaben: Mein Bauvorhaben" +
            "\nLink zum Bauvorhaben: " +
            BASE_URL +
            "/#/bauvorhaben/" +
            bauvorhabenId +
            "\n\nVerknüpfte Abfragen:" +
            "\nAbfrage Alpha: " +
            BASE_URL +
            "/#/abfrage/" +
            abfrageId1 +
            "\nAbfrage Beta: " +
            BASE_URL +
            "/#/abfrage/" +
            abfrageId2 +
            "\n\nDatum des Kommentars: 01.06.2026" +
            "\nText des Kommentars:\nEin Kommentar";

        Mockito.verify(mailSenderRepository, Mockito.times(1)).sendMail(
            List.of(RECEIVER),
            "ISI - Neuer Kommentar zum Bauvorhaben: Mein Bauvorhaben",
            expectedText
        );
    }

    @Test
    void buildEmailText_ohneUrl() {
        final var bauvorhabenId = UUID.randomUUID();
        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.empty());

        final var result = serviceOhneUrl.buildEmailText(
            bauvorhabenId,
            "Mein Bauvorhaben",
            "Ein Kommentar",
            "01.06.2026"
        );

        final var expected =
            "Im Bauvorhaben wurde ein Kommentar gespeichert." +
            "\n\nBauvorhaben: Mein Bauvorhaben" +
            "\n\nDatum des Kommentars: 01.06.2026" +
            "\nText des Kommentars:\nEin Kommentar";

        assertThat(result, is(expected));
    }

    @Test
    void buildEmailText_mitNullWerten() {
        final var bauvorhabenId = UUID.randomUUID();
        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.empty());

        final var result = service.buildEmailText(bauvorhabenId, null, null, null);

        final var expected =
            "Im Bauvorhaben wurde ein Kommentar gespeichert." +
            "\n\nBauvorhaben: " +
            "\nLink zum Bauvorhaben: " +
            BASE_URL +
            "/#/bauvorhaben/" +
            bauvorhabenId +
            "\n\nDatum des Kommentars: " +
            "\nText des Kommentars:\n";

        assertThat(result, is(expected));
    }

    @Test
    void buildAbfragenText_keineBauvorhabenId() {
        final var result = service.buildAbfragenText(null);
        assertThat(result, is(""));
    }

    @Test
    void buildAbfragenText_keineAbfragen() {
        final var bauvorhabenId = UUID.randomUUID();
        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.empty());

        final var result = service.buildAbfragenText(bauvorhabenId);
        assertThat(result, is(""));
    }

    @Test
    void buildAbfragenText_eineAbfrage() {
        final var bauvorhabenId = UUID.randomUUID();
        final var abfrageId = UUID.randomUUID();

        final var abfrage = new Baugenehmigungsverfahren();
        abfrage.setId(abfrageId);
        abfrage.setName("Abfrage Alpha");

        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.of(abfrage));

        final var result = service.buildAbfragenText(bauvorhabenId);

        assertThat(result, is("\n\nVerknüpfte Abfragen:\nAbfrage Alpha: " + BASE_URL + "/#/abfrage/" + abfrageId));
    }

    @Test
    void buildAbfragenText_eineAbfrageOhneUrl() {
        final var bauvorhabenId = UUID.randomUUID();
        final var abfrageId = UUID.randomUUID();

        final var abfrage = new Baugenehmigungsverfahren();
        abfrage.setId(abfrageId);
        abfrage.setName("Abfrage Alpha");

        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.of(abfrage));

        final var result = serviceOhneUrl.buildAbfragenText(bauvorhabenId);

        assertThat(result, is("\n\nVerknüpfte Abfragen:\nAbfrage Alpha"));
    }

    @Test
    void buildAbfragenText_mehrereAbfragen() {
        final var bauvorhabenId = UUID.randomUUID();
        final var abfrageId1 = UUID.randomUUID();
        final var abfrageId2 = UUID.randomUUID();

        final var abfrage1 = new Baugenehmigungsverfahren();
        abfrage1.setId(abfrageId1);
        abfrage1.setName("Abfrage Alpha");

        final var abfrage2 = new Baugenehmigungsverfahren();
        abfrage2.setId(abfrageId2);
        abfrage2.setName("Abfrage Beta");

        Mockito.when(abfrageRepository.findAllByBauvorhabenId(bauvorhabenId)).thenReturn(Stream.of(abfrage1, abfrage2));

        final var result = service.buildAbfragenText(bauvorhabenId);

        assertThat(
            result,
            is(
                "\n\nVerknüpfte Abfragen:" +
                    "\nAbfrage Alpha: " +
                    BASE_URL +
                    "/#/abfrage/" +
                    abfrageId1 +
                    "\nAbfrage Beta: " +
                    BASE_URL +
                    "/#/abfrage/" +
                    abfrageId2
            )
        );
    }
}

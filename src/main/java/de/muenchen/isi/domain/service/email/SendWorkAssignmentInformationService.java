package de.muenchen.isi.domain.service.email;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendWorkAssignmentInformationService {

    private final MailSenderRepository mailSenderRepository;

    private final Environment environment;

    private final String receiverSachbearbeitung;

    private final String receiverBedarfsmeldung;

    public SendWorkAssignmentInformationService(
        @Value("${spring.mail.receiver.sachbearbeitung:}") final String receiverSachbearbeitung,
        @Value("${spring.mail.receiver.bedarfsmeldung:}") final String receiverBedarfsmeldung,
        final MailSenderRepository mailSenderRepository,
        final Environment environment
    ) {
        this.receiverSachbearbeitung = receiverSachbearbeitung;
        this.receiverBedarfsmeldung = receiverBedarfsmeldung;
        this.mailSenderRepository = mailSenderRepository;
        this.environment = environment;
    }

    /**
     * Asynchrone Ausführung der Methode {@link SendWorkAssignmentInformationService#sendWorkAssignmentInformation}.
     *
     * @param abfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     */
    @Async
    public void sendWorkAssignmentInformationAsync(
        final AbfrageModel abfrage,
        final StatusAbfrageEvents stateMachineEvent
    ) {
        this.sendWorkAssignmentInformation(abfrage, stateMachineEvent);
    }

    /**
     * Erstellt den Stadtbezirktext.
     * @param verortung
     * @return Stadtbezirktext zusammengesetzt aus Nummer und Name
     */
    protected String getTextStadtbezirke(VerortungModel verortung) {
        if (verortung == null) {
            return StringUtils.EMPTY;
        }
        return CollectionUtils
            .emptyIfNull(verortung.getStadtbezirke())
            .stream()
            .map(stadtbezirk -> stadtbezirk.getNummer() + "/" + stadtbezirk.getName())
            .collect(Collectors.joining(", "));
    }

    /**
     * Ermittelt die Stadtbezirke auf Basis der jeweiligen Abfrage.
     *
     * @param abfrage
     * @return der Stadtbezirknummer und -name auf Basis der Abfrage oder leerer String falls keine Verortung angegeben worden ist.
     */
    public String getStadtbezirke(AbfrageModel abfrage) {
        String stadtbezirke = "\n\nStadtbezirke: ";
        if (abfrage.getArtAbfrage().equals(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN)) {
            BaugenehmigungsverfahrenModel abfrageTyp = (BaugenehmigungsverfahrenModel) abfrage;
            stadtbezirke += getTextStadtbezirke(abfrageTyp.getVerortung());
        }
        if (abfrage.getArtAbfrage().equals(ArtAbfrage.BAULEITPLANVERFAHREN)) {
            BauleitplanverfahrenModel abfrageTyp = (BauleitplanverfahrenModel) abfrage;
            stadtbezirke += getTextStadtbezirke(abfrageTyp.getVerortung());
        }
        if (abfrage.getArtAbfrage().equals(ArtAbfrage.WEITERES_VERFAHREN)) {
            WeiteresVerfahrenModel abfrageTyp = (WeiteresVerfahrenModel) abfrage;
            stadtbezirke += getTextStadtbezirke(abfrageTyp.getVerortung());
        }

        return stadtbezirke;
    }

    /**
     * Versendet die Email zur Bearbeitungsinformation.
     *
     * Anhand der gegebenen Statusübergangsinformation wird entweder eine Email versendet oder ein Emailversand unterlassen.
     *
     * Der Emailtext ergibt sich aus den in den Parametern gegebenen Informationen.
     *
     * @param abfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     */
    public void sendWorkAssignmentInformation(final AbfrageModel abfrage, final StatusAbfrageEvents stateMachineEvent) {
        final var receiverEmailAddresses = getReceiver(abfrage, stateMachineEvent);
        if (CollectionUtils.isNotEmpty(receiverEmailAddresses)) {
            final var subject = getSubject(abfrage.getName(), stateMachineEvent);
            var text = getText(abfrage.getName(), stateMachineEvent)
                .concat(StringUtils.defaultIfEmpty(getStadtbezirke(abfrage), StringUtils.EMPTY));
            mailSenderRepository.sendMail(receiverEmailAddresses, subject, text);
        }
    }

    /**
     * Ermittelt den Empfänger der Email auf Basis der Statusübergangsinformation und der Abfrage.
     *
     * @param abfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     * @return der Emailempfäger auf Basis der Statusübergangsinformation oder null falls für den gegebenen Statusübergang kein Emailversand vorgesehen ist.
     */
    protected List<String> getReceiver(final AbfrageModel abfrage, final StatusAbfrageEvents stateMachineEvent) {
        if (StatusAbfrageEvents.FREIGABE.equals(stateMachineEvent)) {
            return List.of(receiverSachbearbeitung);
        } else if (StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.equals(stateMachineEvent)) {
            return List.of(receiverSachbearbeitung);
        } else if (StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME.equals(stateMachineEvent)) {
            return List.of(receiverBedarfsmeldung);
        } else if (StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE.equals(stateMachineEvent)) {
            final var bearbeitungshistorie = abfrage.getBearbeitungshistorie();
            final var mailOfPerson = getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(bearbeitungshistorie);
            return List.of(mailOfPerson);
        } else if (StatusAbfrageEvents.KEINE_BEARBEITUNG_NOETIG.equals(stateMachineEvent)) {
            // Erledigt durch Sachbearbeitung
            return List.of(receiverSachbearbeitung);
        } else if (StatusAbfrageEvents.SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG.equals(stateMachineEvent)) {
            // Erledigt durch Fachreferat
            return List.of(receiverBedarfsmeldung);
        }
        return null;
    }

    /**
     * Ermittelt die Emailadresse der {@link BearbeitendePersonModel} welche die Abfrage initial erstellt hat.
     *
     * @param bearbeitungshistorie zur Ermittlung der Emailadresse.
     * @return die Emailadresse oder null falls keine Adresse ermittelt werden kann.
     */
    protected String getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(
        final List<BearbeitungshistorieModel> bearbeitungshistorie
    ) {
        return CollectionUtils
            .emptyIfNull(bearbeitungshistorie)
            .stream()
            .filter(b -> StatusAbfrage.OFFEN.equals(b.getZielStatus()))
            .map(BearbeitungshistorieModel::getBearbeitendePerson)
            .filter(ObjectUtils::isNotEmpty)
            .map(BearbeitendePersonModel::getEmail)
            .filter(StringUtils::isNotEmpty)
            .findFirst()
            .orElse(null);
    }

    /**
     * Erstellt den Emailtext.
     *
     * @param nameAbfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     * @return der Emailtext zusammengesetzt aus dem {@link StatusAbfrageEvents#getPropertyWorkAssignmentInformationText()} und dem Namen der Abfrage.
     */
    protected String getText(final String nameAbfrage, final StatusAbfrageEvents stateMachineEvent) {
        return getText(stateMachineEvent).concat(StringUtils.defaultIfEmpty(nameAbfrage, StringUtils.EMPTY));
    }

    /**
     * Erstellt den Emailbetreff.
     *
     * @param nameAbfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     * @return der Betreff zusammengesetzt aus der {@link StatusAbfrageEvents#getPropertyWorkAssignmentInformationSubject()} und dem Namen der Abfrage.
     */
    protected String getSubject(final String nameAbfrage, final StatusAbfrageEvents stateMachineEvent) {
        return getSubject(stateMachineEvent).concat(StringUtils.defaultIfEmpty(nameAbfrage, StringUtils.EMPTY));
    }

    private String getText(final StatusAbfrageEvents stateMachineEvent) {
        return environment.getProperty(stateMachineEvent.getPropertyWorkAssignmentInformationText(), StringUtils.EMPTY);
    }

    private String getSubject(final StatusAbfrageEvents stateMachineEvent) {
        return environment.getProperty(
            stateMachineEvent.getPropertyWorkAssignmentInformationSubject(),
            StringUtils.EMPTY
        );
    }
}

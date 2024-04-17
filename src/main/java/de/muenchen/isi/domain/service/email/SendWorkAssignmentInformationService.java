package de.muenchen.isi.domain.service.email;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.common.BearbeitendePersonModel;
import de.muenchen.isi.domain.model.common.BearbeitungshistorieModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendWorkAssignmentInformationService {

    private final MailSenderRepository mailSenderRepository;

    private final String receiverSachbearbeitung;

    private final String receiverBedarfsmeldung;

    public SendWorkAssignmentInformationService(
        @Value("${spring.mail.receiver.sachbearbeitung:}") final String receiverSachbearbeitung,
        @Value("${spring.mail.receiver.bedarfsmeldung:}") final String receiverBedarfsmeldung,
        final MailSenderRepository mailSenderRepository
    ) {
        this.receiverSachbearbeitung = receiverSachbearbeitung;
        this.receiverBedarfsmeldung = receiverBedarfsmeldung;
        this.mailSenderRepository = mailSenderRepository;
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
        final var reveiverEmailAddress = getReceiver(abfrage, stateMachineEvent);
        if (StringUtils.isNotEmpty(reveiverEmailAddress)) {
            final var subject = getSubject(abfrage.getName(), stateMachineEvent);
            final var text = getText(abfrage.getName(), stateMachineEvent);
            mailSenderRepository.sendMail(reveiverEmailAddress, subject, text);
        }
    }

    /**
     * Ermittelt den Empfänger der Email auf Basis der Statusübergangsinformation und der Abfrage.
     *
     * @param abfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     * @return der Emailempfäger auf Basis der Statusübergangsinformation oder null falls für den gegebenen Statusübergang kein Emailversand vorgesehen ist.
     */
    protected String getReceiver(final AbfrageModel abfrage, final StatusAbfrageEvents stateMachineEvent) {
        if (StatusAbfrageEvents.FREIGABE.equals(stateMachineEvent)) {
            return receiverSachbearbeitung;
        } else if (StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.equals(stateMachineEvent)) {
            return receiverSachbearbeitung;
        } else if (StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME.equals(stateMachineEvent)) {
            return receiverBedarfsmeldung;
        } else if (StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE.equals(stateMachineEvent)) {
            final var bearbeitungshistorie = abfrage.getBearbeitungshistorie();
            return getEmailAddressOfPersonWhichInitiallyCreatedTheAbfrage(bearbeitungshistorie);
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
     * @param stateMachineEvent
     * @return der Emailtext zusammengesetzt aus dem {@link StatusAbfrageEvents#getInformationText()} und dem Namen der Abfrage.
     */
    protected String getText(final String nameAbfrage, final StatusAbfrageEvents stateMachineEvent) {
        return stateMachineEvent
            .getInformationText()
            .concat("\n\n")
            .concat("Abfrage: ")
            .concat(StringUtils.defaultIfEmpty(nameAbfrage, ""));
    }

    /**
     * Erstellt den Emailbetreff.
     *
     * @param nameAbfrage
     * @param stateMachineEvent
     * @return der Betreff zusammengesetzt aus der Statusübergangsinformation und dem Namen der Abfrage.
     */
    protected String getSubject(final String nameAbfrage, final StatusAbfrageEvents stateMachineEvent) {
        return stateMachineEvent
            .getButtonName()
            .concat(" - ")
            .concat("Abfrage: ")
            .concat(StringUtils.defaultIfEmpty(nameAbfrage, ""));
    }
}

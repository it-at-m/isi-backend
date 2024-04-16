package de.muenchen.isi.domain.service.email;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendWorkAssignmentInformationService {

    private final MailSenderRepository mailSenderRepository;

    private final AuthenticationUtils authenticationUtils;

    private final String receiverSachbearbeitung;

    private final String receiverBedarfsmeldung;

    public SendWorkAssignmentInformationService(
        @Value("${spring.mail.receiver.sachbearbeitung:}") final String receiverSachbearbeitung,
        @Value("${spring.mail.receiver.bedarfsmeldung:}") final String receiverBedarfsmeldung,
        final MailSenderRepository mailSenderRepository,
        final AuthenticationUtils authenticationUtils
    ) {
        this.receiverSachbearbeitung = receiverSachbearbeitung;
        this.receiverBedarfsmeldung = receiverBedarfsmeldung;
        this.mailSenderRepository = mailSenderRepository;
        this.authenticationUtils = authenticationUtils;
    }

    /**
     *
     * @param nameAbfrage
     * @param stateMachineEvent
     */
    @Async
    public void sendWorkAssignmentInformationAsync(
        final String nameAbfrage,
        final StatusAbfrageEvents stateMachineEvent
    ) {
        this.sendWorkAssignmentInformation(nameAbfrage, stateMachineEvent);
    }

    /**
     * Versendet die Email zur Bearbeitungsinformation.
     *
     * Anhand der gegebenen Statusübergangsinformation wird entweder eine Email versendet oder ein Emailversand unterlassen.
     *
     * Der Emailtext ergibt sich aus den in den Parametern gegebenen Informationen.
     *
     * @param nameAbfrage
     * @param stateMachineEvent als Statusübergangsinformation.
     */
    public void sendWorkAssignmentInformation(final String nameAbfrage, final StatusAbfrageEvents stateMachineEvent) {
        final var reveiverEmailAddress = getReceiver(stateMachineEvent);
        if (StringUtils.isNotEmpty(reveiverEmailAddress)) {
            final var subject = getSubject(nameAbfrage, stateMachineEvent);
            final var text = getText(nameAbfrage, stateMachineEvent);
            mailSenderRepository.sendMail(reveiverEmailAddress, subject, text);
        }
    }

    /**
     * Ermittelt den Empfänger der Email auf Basis der Statusübergangsinformation.
     *
     * @param stateMachineEvent als Statusübergangsinformation.
     * @return der Emailempfäger auf Basis der Statusübergangsinformation oder null falls für den gegebenen Statusübergang kein Emailversand vorgesehen ist.
     */
    protected String getReceiver(final StatusAbfrageEvents stateMachineEvent) {
        if (StatusAbfrageEvents.FREIGABE.equals(stateMachineEvent)) {
            return receiverSachbearbeitung;
        } else if (StatusAbfrageEvents.ERNEUTE_BEARBEITUNG.equals(stateMachineEvent)) {
            return receiverSachbearbeitung;
        } else if (StatusAbfrageEvents.VERSCHICKEN_DER_STELLUNGNAHME.equals(stateMachineEvent)) {
            return receiverBedarfsmeldung;
        } else if (StatusAbfrageEvents.BEDARFSMELDUNG_ERFOLGTE.equals(stateMachineEvent)) {
            return authenticationUtils.getEmail();
        }
        return null;
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

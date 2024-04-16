package de.muenchen.isi.domain.service.email;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendWorkAssignmentInformationService {

    private final MailSenderRepository mailSenderRepository;

    private final AuthenticationUtils authenticationUtils;

    private final String receiverSachbearbeitung;

    private final String receiverBedarfsmeldung;

    public SendWorkAssignmentInformationService(
        @Value("${spring.mail.distribution-list.sachbearbeitung:}") final String receiverSachbearbeitung,
        @Value("${spring.mail.distribution-list.bedarfsmeldung:}") final String receiverBedarfsmeldung,
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
     * @param abfrage
     * @param stateMachineEvent
     */
    public void sendWorkAssignmentInformation(final AbfrageModel abfrage, final StatusAbfrageEvents stateMachineEvent) {
        final var reveiverEmailAddress = getReceiver(stateMachineEvent);
        if (StringUtils.isNotEmpty(reveiverEmailAddress)) {
            final var subject = getSubject(stateMachineEvent);
            final var text = getText(stateMachineEvent);
            mailSenderRepository.sendMailAsync(reveiverEmailAddress, subject, text);
        }
    }

    /**
     *
     * @param stateMachineEvent
     * @return
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

    protected String getText(final StatusAbfrageEvents stateMachineEvent) {
        return "";
    }

    protected String getSubject(final StatusAbfrageEvents stateMachineEvent) {
        return "";
    }
}

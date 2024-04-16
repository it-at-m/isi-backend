package de.muenchen.isi.domain.service.email;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendWorkAssignmentInformationService {

    private final MailSenderRepository mailSenderRepository;

    protected String getReceiverForMessage(final AbfrageModel model) {
        final var mailAddressReceiver = "test";

        return mailAddressReceiver;
    }
}

package de.muenchen.isi.infrastructure.repository.email;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MailSenderRepository {

    private final JavaMailSender mailSender;

    private final String fromEmailAddress;

    public MailSenderRepository(
        final JavaMailSender mailSender,
        @Value("${spring.mail.username:}") final String fromEmailAddress
    ) {
        this.mailSender = mailSender;
        this.fromEmailAddress = fromEmailAddress;
    }

    /**
     * Versendet eine Email mit den in den Parameter gegebenen Informationen.
     *
     * Tritt beim Emailversand ein Fehler auf, so wird dieser als "error" geloggt.
     *
     * @param receiver als Empfänger der Email.
     * @param subject als Betreff der Email.
     * @param text als Text der Email.
     */
    public void sendMail(final List<String> receiver, final String subject, final String text) {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmailAddress);
        mailMessage.setTo(ArrayUtils.toStringArray(receiver.toArray()));
        mailMessage.setSubject(sanitizeHeaderValue(subject));
        mailMessage.setText(text);
        try {
            mailSender.send(mailMessage);
        } catch (final MailSendException exception) {
            final var message = "Die Email konnte nicht versendet werden. Anzahl Empfänger: %d".formatted(
                receiver.size()
            );
            log.error(message, exception);
        } catch (final MailAuthenticationException exception) {
            final var message = "Die Email konnte wegen fehlerhafter Emailcredentials nicht versendet werden.";
            log.error(message, exception);
        } catch (final MailParseException exception) {
            final var message = "Die Email konnte wegen fehlerhafter Email-Parameter nicht versendet werden.";
            log.error(message, exception);
        } catch (final Exception exception) {
            final var message = "Beim Emailversand ist ein Fehler aufgetreten.";
            log.error(message, exception);
        }
    }

    private String sanitizeHeaderValue(final String value) {
        return StringUtils.defaultString(value).replaceAll("[\\r\\n]", " ");
    }
}

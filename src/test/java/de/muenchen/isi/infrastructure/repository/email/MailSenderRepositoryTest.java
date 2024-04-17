package de.muenchen.isi.infrastructure.repository.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
@MockitoSettings(strictness = Strictness.LENIENT)
class MailSenderRepositoryTest {

    @Mock
    private JavaMailSender javaMailSender;

    private MailSenderRepository mailSenderRepository;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        this.mailSenderRepository = new MailSenderRepository(javaMailSender, "fromEmailAddress");
        Mockito.reset(javaMailSender);
    }

    @Test
    void sendMail() {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        mailSenderRepository.sendMail("receiver", "subject", "text");

        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailMailSendException(final CapturedOutput output) {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new MailSendException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        assertThat(
            output.getAll(),
            containsString("Die Email konnte nicht an den Empfänger receiver versendet werden.")
        );
        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailMailAuthenticationException(final CapturedOutput output) {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new MailAuthenticationException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        assertThat(
            output.getAll(),
            containsString("Die Email konnte wegen fehlerhafter Emailcredentials nicht versendet werden.")
        );
        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailMailParseException(final CapturedOutput output) {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new MailParseException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        assertThat(
            output.getAll(),
            containsString("Die Email konnte wegen fehlerhafter Email-Parameter nicht versendet werden.")
        );
        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailException(final CapturedOutput output) {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new RuntimeException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        assertThat(output.getAll(), containsString("Beim Emailversand ist ein Fehler aufgetreten."));
        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }
}

package de.muenchen.isi.infrastructure.repository.email;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.service.email.SendWorkAssignmentInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
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
    void sendMailMailSendException() {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new MailSendException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailMailAuthenticationException() {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new MailAuthenticationException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailMailParseException() {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new MailParseException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }

    @Test
    void sendMailException() {
        final var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("fromEmailAddress");
        mailMessage.setTo("receiver");
        mailMessage.setSubject("subject");
        mailMessage.setText("text");

        Mockito.doThrow(new RuntimeException("test")).when(javaMailSender).send(mailMessage);

        mailSenderRepository.sendMail("receiver", "subject", "text");

        Mockito.verify(javaMailSender, Mockito.times(1)).send(mailMessage);
    }
}

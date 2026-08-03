package de.muenchen.isi.domain.service.email;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.email.MailSenderRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SendKommentarBauvorhabenNotificationService {

    private final MailSenderRepository mailSenderRepository;

    private final AbfrageRepository abfrageRepository;

    private final String receiverKommentarBauvorhaben;

    private final String isiEnvironmentUrl;

    public SendKommentarBauvorhabenNotificationService(
        @Value("${spring.mail.receiver.kommentar-bauvorhaben:}") final String receiverKommentarBauvorhaben,
        @Value("${isi.environment.url:}") final String isiEnvironmentUrl,
        final MailSenderRepository mailSenderRepository,
        final AbfrageRepository abfrageRepository
    ) {
        this.receiverKommentarBauvorhaben = receiverKommentarBauvorhaben;
        this.isiEnvironmentUrl = isiEnvironmentUrl;
        this.mailSenderRepository = mailSenderRepository;
        this.abfrageRepository = abfrageRepository;
    }

    @Async
    @Transactional(readOnly = true)
    public void sendKommentarBauvorhabenNotificationAsync(
        final UUID bauvorhabenId,
        final String bauvorhabenName,
        final String kommentarText,
        final LocalDate kommentarDatum,
        final boolean isNew
    ) {
        sendKommentarBauvorhabenNotification(bauvorhabenId, bauvorhabenName, kommentarText, kommentarDatum, isNew);
    }

    public void sendKommentarBauvorhabenNotification(
        final UUID bauvorhabenId,
        final String bauvorhabenName,
        final String kommentarText,
        final LocalDate kommentarDatum,
        final boolean isNew
    ) {
        if (StringUtils.isEmpty(receiverKommentarBauvorhaben)) {
            return;
        }
        final var subjectAction = isNew ? "Neuer" : "Aktualisierter";
        final var subject =
            "ISI - " + subjectAction + " Kommentar zum Bauvorhaben: " + StringUtils.defaultIfEmpty(bauvorhabenName, "");
        final var text = buildEmailText(
            bauvorhabenId,
            bauvorhabenName,
            kommentarText,
            kommentarDatum != null ? kommentarDatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : null,
            isNew
        );
        mailSenderRepository.sendMail(List.of(receiverKommentarBauvorhaben), subject, text);
    }

    protected String buildEmailText(
        final UUID bauvorhabenId,
        final String bauvorhabenName,
        final String kommentarText,
        final String kommentarDatum,
        final boolean isNew
    ) {
        final var sb = new StringBuilder();
        final var bodyAction = isNew ? "gespeichert" : "aktualisiert";
        sb.append("Im Bauvorhaben wurde ein Kommentar ").append(bodyAction).append(".");
        sb.append("\n\nBauvorhaben: ").append(StringUtils.defaultIfEmpty(bauvorhabenName, ""));
        if (StringUtils.isNotEmpty(isiEnvironmentUrl) && bauvorhabenId != null) {
            sb
                .append("\nLink zum Bauvorhaben: ")
                .append(isiEnvironmentUrl)
                .append("/#/bauvorhaben/")
                .append(bauvorhabenId);
        }
        sb.append(buildAbfragenText(bauvorhabenId));
        sb.append("\n\nDatum des Kommentars: ").append(StringUtils.defaultIfEmpty(kommentarDatum, ""));
        sb.append("\nText des Kommentars:\n").append(StringUtils.defaultIfEmpty(kommentarText, ""));
        return sb.toString();
    }

    protected String buildAbfragenText(final UUID bauvorhabenId) {
        if (bauvorhabenId == null) {
            return StringUtils.EMPTY;
        }
        final List<Abfrage> abfragen = abfrageRepository
            .findAllByBauvorhabenId(bauvorhabenId)
            .collect(Collectors.toList());
        if (abfragen.isEmpty()) {
            return StringUtils.EMPTY;
        }
        final var sb = new StringBuilder("\n\nVerknüpfte Abfragen:");
        for (final Abfrage abfrage : abfragen) {
            sb.append("\n").append(StringUtils.defaultIfEmpty(abfrage.getName(), ""));
            if (StringUtils.isNotEmpty(isiEnvironmentUrl) && abfrage.getId() != null) {
                sb.append(": ").append(isiEnvironmentUrl).append("/#/abfrage/").append(abfrage.getId());
            }
        }
        return sb.toString();
    }
}

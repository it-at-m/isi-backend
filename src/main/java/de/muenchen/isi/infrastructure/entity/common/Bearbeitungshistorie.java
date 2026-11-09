package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.enums.BearbeitungsArt;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.security.AuthenticationUtils;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Embeddable
@Data
public class Bearbeitungshistorie {

    @Enumerated(EnumType.STRING)
    @Column
    private BearbeitungsArt art;

    /**
     * Der Zielstatus einer Statusänderung bzw. der Anfangsstatus einer erstellten Abfrage. Sonst null.
     */
    @Enumerated(EnumType.STRING)
    @Column
    private StatusAbfrage zielStatus;

    @Column
    private LocalDateTime zeitpunkt;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "bearbeitende_person_name")),
        @AttributeOverride(name = "email", column = @Column(name = "bearbeitende_person_email")),
        @AttributeOverride(
            name = "organisationseinheit",
            column = @Column(name = "bearbeitende_person_organisationseinheit")
        ),
    })
    private BearbeitendePerson bearbeitendePerson;

    /**
     * Ermittelt die Emailadressen aller Personen aus der Bearbeitungshistorie in Kleinbuchstaben.
     * <p>
     * Der Platzhalter für Nutzer ohne Emailadresse identifiziert keine Person und wird deshalb nicht berücksichtigt.
     *
     * @param bearbeitungshistorie zur Ermittlung der Emailadressen.
     * @return die Emailadressen ohne Duplikate.
     */
    public static Set<String> getEmails(final List<Bearbeitungshistorie> bearbeitungshistorie) {
        return CollectionUtils.emptyIfNull(bearbeitungshistorie)
            .stream()
            .map(Bearbeitungshistorie::getBearbeitendePerson)
            .filter(Objects::nonNull)
            .map(BearbeitendePerson::getEmail)
            .filter(StringUtils::isNotBlank)
            .map(email -> email.toLowerCase(Locale.ROOT))
            .filter(email -> !AuthenticationUtils.UNAUTHENTICATED_USER.equals(email))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

package de.muenchen.isi.infrastructure.adapter.listener;

import de.muenchen.isi.infrastructure.entity.common.Kommentar;
import de.muenchen.isi.security.AuthenticationUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KommentarListener {

    private final AuthenticationUtils authenticationUtils;

    @PrePersist
    @PreUpdate
    public void beforeSave(final Kommentar kommentar) {
        final var bearbeitendePerson = authenticationUtils.getBearbeitendePerson();
        if (kommentar.getErstellungsdatum() == null) {
            kommentar.setErstellungsdatum(LocalDate.now());
        }
        kommentar.setBearbeitendePerson(bearbeitendePerson);
    }
}

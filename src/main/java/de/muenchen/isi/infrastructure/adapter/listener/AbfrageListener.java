package de.muenchen.isi.infrastructure.adapter.listener;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbfrageListener {

    private final AuthenticationUtils authenticationUtils;

    private final AbfrageRepository abfrageRepository;

    @PrePersist
    @PreUpdate
    public void beforeSave(final Abfrage abfrage) {
        final var optAbfrage = abfrageRepository.findById(abfrage.getId());
        final var statusAbfrageToSave = abfrage.getStatusAbfrage();
        if (optAbfrage.isPresent()) {
            final var statusSavedAbfrage = optAbfrage.get().getStatusAbfrage();
            if (statusSavedAbfrage != statusAbfrageToSave) {
                final var bearbeitungshistorie = createBearbeitungshistorieForStatus(statusAbfrageToSave);
                abfrage.getBearbeitungshistorie().add(bearbeitungshistorie);
            }
        } else {
            final var bearbeitungshistorie = createBearbeitungshistorieForStatus(statusAbfrageToSave);
            abfrage.getBearbeitungshistorie().add(bearbeitungshistorie);
        }
    }

    protected Bearbeitungshistorie createBearbeitungshistorieForStatus(final StatusAbfrage status) {
        final var bearbeitendePerson = authenticationUtils.getBearbeitendePerson();
        final var bearbeitungshistorie = new Bearbeitungshistorie();
        bearbeitungshistorie.setZielStatus(status);
        bearbeitungshistorie.setZeitpunkt(LocalDateTime.now());
        bearbeitungshistorie.setBearbeitendePerson(bearbeitendePerson);
        return bearbeitungshistorie;
    }
}

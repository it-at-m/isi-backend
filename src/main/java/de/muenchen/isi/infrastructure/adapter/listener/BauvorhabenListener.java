package de.muenchen.isi.infrastructure.adapter.listener;

import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.security.AuthenticationUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BauvorhabenListener {

    private final AuthenticationUtils authenticationUtils;

    @PrePersist
    @PreUpdate
    public void beforeSave(final Bauvorhaben bauvorhaben) {
        final var bearbeitendePerson = authenticationUtils.getBearbeitendePerson();
        bauvorhaben.setBearbeitendePerson(bearbeitendePerson);
    }
}

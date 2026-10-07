package de.muenchen.isi.infrastructure.repository.benutzer;

import de.muenchen.isi.infrastructure.entity.benutzer.Benutzer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenutzerRepository extends JpaRepository<Benutzer, UUID> {
    Optional<Benutzer> findByPersonalID(String personalId);
}

package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.Umlegung;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmrechnungUmlegungRepository extends JpaRepository<Umlegung, UUID> {
    Optional<Umlegung> findBybezeichnung(final String bezeichnung);
}

package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmlegungFoerderartenRepository extends JpaRepository<UmlegungFoerderarten, UUID> {
    Optional<UmlegungFoerderarten> findBybezeichnung(final String bezeichnung);
}

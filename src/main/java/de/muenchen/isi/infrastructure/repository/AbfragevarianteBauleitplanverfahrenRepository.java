package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteBauleitplanverfahrenRepository
    extends JpaRepository<AbfragevarianteBauleitplanverfahren, UUID> {
    @Query(
        value = "SELECT abfragevarianten_bauleitplanverfahren_id FROM abfragevariante_bauleitplanverfahren WHERE id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteById(final String id);

    @Query(
        value = "SELECT abfragevarianten_sachbearbeitung_bauleitplanverfahren_id FROM abfragevariante_bauleitplanverfahren WHERE id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteSachbearbeitungById(final String id);
}

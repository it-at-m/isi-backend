package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteBauleitplanverfahrenRepository
    extends JpaRepository<AbfragevarianteBauleitplanverfahren, UUID> {
    @Query(
        value = "select bauleitplanverfahren_abfragevarianten_id from abfragevariante_bauleitplanverfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteById(final String id);

    @Query(
        value = "select bauleitplanverfahren_abfragevarianten_sachbearbeitung_id from abfragevariante_bauleitplanverfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteSachbearbeitungById(final String id);
}

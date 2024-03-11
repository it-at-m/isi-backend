package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteBauleitplanverfahrenRepository
    extends JpaRepository<AbfragevarianteBauleitplanverfahren, UUID> {
    @Query(
        value = "select abfrgvar_bauleitplnvrfhrn_id from abfrgvar_bauleitplnvrfhrn where id = ?1",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @Query(
        value = "select abfrgvar_schbrbtng_bauleitplnvrfhrn_id from abfrgvar_bauleitplnvrfhrn where id = ?1",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBaugenehmigungsverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteBaugenehmigungsverfahrenRepository
    extends JpaRepository<AbfragevarianteBaugenehmigungsverfahren, UUID> {
    @Query(
        value = "select abfrgvar_baugnhmgsverfhrn_id from abfrgvar_baugnhmgsverfhrn where id = ?1",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @Query(
        value = "select abfrgvar_schbrbtng_baugnhmgsverfhrn_id_index from abfrgvar_baugnhmgsverfhrn where id = ?1",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

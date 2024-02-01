package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBaugenehmigungsverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteBaugenehmigungsverfahrenRepository
    extends JpaRepository<AbfragevarianteBaugenehmigungsverfahren, UUID> {
    @Query(
        value = "select abfragevarianten_baugenehmigungsverfahren_id from abfragevariante_baugenehmigungsverfahren where CAST(id AS uuid) = :id",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @Query(
        value = "select abfragevarianten_sachbearbeitung_baugenehmigungsverfahren_id from abfragevariante_baugenehmigungsverfahren where CAST(id AS uuid) = :id",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

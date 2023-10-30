package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBaugenehmigungsverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteBaugenehmigungsverfahrenRepository
    extends JpaRepository<AbfragevarianteBaugenehmigungsverfahren, UUID> {
    @Query(
        value = "select baugenehmigungsverfahren_abfragevarianten_id from abfragevariante_baugenehmigungsverfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteById(final String id);

    @Query(
        value = "select baugenehmigungsverfahren_abfragevarianten_sachbearbeitung_id from abfragevariante_baugenehmigungsverfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteSachbearbeitungById(final String id);
}

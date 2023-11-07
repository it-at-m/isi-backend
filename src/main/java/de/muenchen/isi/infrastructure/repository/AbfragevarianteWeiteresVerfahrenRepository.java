package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteWeiteresVerfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteWeiteresVerfahrenRepository
    extends JpaRepository<AbfragevarianteWeiteresVerfahren, UUID> {
    @Query(
        value = "select abfragevarianten_weiteres_verfahren_id from abfragevariante_weiteres_verfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteById(final String id);

    @Query(
        value = "select abfragevarianten_sachbearbeitung_weiteres_verfahren_id from abfragevariante_weiteres_verfahren where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageIdForAbfragevarianteSachbearbeitungById(final String id);
}
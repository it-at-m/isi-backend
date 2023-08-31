package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteRepository extends JpaRepository<Abfragevariante, UUID> {
    @Query(value = "select abfrage_abfragevarianten_id from abfragevariante where id = ?1", nativeQuery = true)
    Optional<String> findAbfrageAbfragevariantenIdById(String id);

    @Query(
        value = "select abfrage_abfragevarianten_sachbearbeitung_id FROM abfragevariante where id = ?1",
        nativeQuery = true
    )
    Optional<String> findAbfrageAbfragevariantenSachbearbeitungIdById(String id);
}

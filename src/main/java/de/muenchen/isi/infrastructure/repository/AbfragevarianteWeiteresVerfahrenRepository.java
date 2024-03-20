package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteWeiteresVerfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbfragevarianteWeiteresVerfahrenRepository
    extends JpaRepository<AbfragevarianteWeiteresVerfahren, UUID> {
    @Query(
        value = "select abfrgvar_weitrs_vrfhrn_id from abfrgvar_weitrs_vrfhrn where CAST(id as uuid) = CAST(:id as uuid)",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @Query(
        value = "select abfrgvar_schbrbtng_weitrs_vrfhrn_id from abfrgvar_weitrs_vrfhrn where CAST(id as uuid) = CAST(:id as uuid)",
        nativeQuery = true
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

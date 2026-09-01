package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AbfragevarianteBauleitplanverfahrenRepository
    extends JpaRepository<AbfragevarianteBauleitplanverfahren, UUID>
{
    @NativeQuery(
        "select abfrgvar_bauleitplnvrfhrn_id from abfrgvar_bauleitplnvrfhrn where CAST(id as uuid) = CAST(:id as uuid)"
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @NativeQuery(
        "select abfrgvar_schbrbtng_bauleitplnvrfhrn_id from abfrgvar_bauleitplnvrfhrn where CAST(id as uuid) = CAST(:id as uuid)"
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

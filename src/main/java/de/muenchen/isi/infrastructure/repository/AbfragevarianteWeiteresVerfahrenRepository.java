package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteWeiteresVerfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AbfragevarianteWeiteresVerfahrenRepository
    extends JpaRepository<AbfragevarianteWeiteresVerfahren, UUID>
{
    @NativeQuery(
        "select abfrgvar_weitrs_vrfhrn_id from abfrgvar_weitrs_vrfhrn where CAST(id as uuid) = CAST(:id as uuid)"
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @NativeQuery(
        "select abfrgvar_schbrbtng_weitrs_vrfhrn_id from abfrgvar_weitrs_vrfhrn where CAST(id as uuid) = CAST(:id as uuid)"
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

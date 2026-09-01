package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.AbfragevarianteBaugenehmigungsverfahren;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AbfragevarianteBaugenehmigungsverfahrenRepository
    extends JpaRepository<AbfragevarianteBaugenehmigungsverfahren, UUID>
{
    @NativeQuery(
        "select abfrgvar_baugnhmgsverfhrn_id from abfrgvar_baugnhmgsverfhrn where CAST(id as uuid) = CAST(:id as uuid)"
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteById(final UUID id);

    @NativeQuery(
        "select abfrgvar_schbrbtng_baugnhmgsverfhrn_id from abfrgvar_baugnhmgsverfhrn where CAST(id as uuid) = CAST(:id as uuid)"
    )
    Optional<UUID> findAbfrageIdForAbfragevarianteSachbearbeitungById(final UUID id);
}

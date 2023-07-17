package de.muenchen.isi.infrastructure.repository.search;

import de.muenchen.isi.infrastructure.entity.search.Suchwort;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuchwortRepository extends JpaRepository<Suchwort, UUID> {
    void deleteAllByReferenceId(final UUID referenceId);
}

package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.BauvorhabenSuchwort;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BauvorhabenSuchwortRepository extends JpaRepository<BauvorhabenSuchwort, UUID> {
    void deleteAllByReferenceId(final UUID referenceId);
}

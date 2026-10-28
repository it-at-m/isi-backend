package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BauvorhabenRepository extends JpaRepository<Bauvorhaben, UUID> {
    /**
     * Liefert ausschließlich den Ersteller, ohne das vollständige Bauvorhaben samt Verortung zu laden.
     */
    @Query("SELECT b.sub FROM Bauvorhaben b WHERE b.id = :id")
    Optional<String> findSubById(@Param("id") final UUID id);
}

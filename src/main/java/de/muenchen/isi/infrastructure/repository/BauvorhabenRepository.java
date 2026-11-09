package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BauvorhabenRepository extends JpaRepository<Bauvorhaben, UUID> {
    /**
     * Liefert ausschließlich die Bearbeitungshistorie, ohne die vollständige Entität samt Verortung zu laden.
     */
    @Query("SELECT h FROM Bauvorhaben e JOIN e.bearbeitungshistorie h WHERE e.id = :id ORDER BY h.zeitpunkt ASC")
    List<Bearbeitungshistorie> findBearbeitungshistorieById(@Param("id") final UUID id);
}

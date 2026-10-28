/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InfrastruktureinrichtungRepository extends JpaRepository<Infrastruktureinrichtung, UUID> {
    Stream<Infrastruktureinrichtung> findAllByBauvorhabenId(final UUID id);

    /**
     * Liefert ausschließlich den Ersteller, ohne die vollständige Infrastruktureinrichtung samt Verortung zu laden.
     */
    @Query("SELECT i.sub FROM Infrastruktureinrichtung i WHERE i.id = :id")
    Optional<String> findSubById(@Param("id") final UUID id);
}

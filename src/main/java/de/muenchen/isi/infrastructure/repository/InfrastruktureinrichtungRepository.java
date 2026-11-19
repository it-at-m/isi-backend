/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InfrastruktureinrichtungRepository extends JpaRepository<Infrastruktureinrichtung, UUID> {
    Stream<Infrastruktureinrichtung> findAllByBauvorhabenId(final UUID id);

    /**
     * Ermittelt ausschließlich die Namen der Infrastruktureinrichtungen eines Bauvorhabens, ohne die
     * Infrastruktureinrichtungen samt Abhängigkeiten zu laden.
     *
     * @param bauvorhabenId des Bauvorhabens
     * @return die Namen der Infrastruktureinrichtungen, welche das Bauvorhaben referenzieren.
     */
    @Query(
        "SELECT infrastruktureinrichtung.nameEinrichtung FROM Infrastruktureinrichtung infrastruktureinrichtung WHERE infrastruktureinrichtung.bauvorhaben.id = :bauvorhabenId"
    )
    List<String> findNamesEinrichtungByBauvorhabenId(@Param("bauvorhabenId") final UUID bauvorhabenId);
}

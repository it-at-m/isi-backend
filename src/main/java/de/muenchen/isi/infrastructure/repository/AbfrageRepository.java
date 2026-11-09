/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbfrageRepository extends JpaRepository<Abfrage, UUID> {
    Stream<Abfrage> findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(final UUID id);

    /**
     * Ermittelt ausschließlich den Status einer Abfrage, ohne die Abfrage samt Abhängigkeiten zu laden.
     *
     * @param id der Abfrage
     * @return den Status der Abfrage oder ein leeres {@link Optional} falls die Abfrage nicht existiert.
     */
    @Query("SELECT abfrage.statusAbfrage FROM Abfrage abfrage WHERE abfrage.id = :id")
    Optional<StatusAbfrage> findStatusAbfrageById(@Param("id") final UUID id);

    /**
     * Ermittelt ausschließlich die Namen der Abfragen eines Bauvorhabens, ohne die Abfragen samt Abhängigkeiten zu laden.
     *
     * @param bauvorhabenId des Bauvorhabens
     * @return die Namen der Abfragen, welche das Bauvorhaben referenzieren.
     */
    @Query("SELECT abfrage.name FROM Abfrage abfrage WHERE abfrage.bauvorhaben.id = :bauvorhabenId")
    List<String> findNamesByBauvorhabenId(@Param("bauvorhabenId") final UUID bauvorhabenId);

    /**
     * Ermittelt ausschließlich ID und Namen der Abfragen eines Bauvorhabens, ohne die Abfragen samt Abhängigkeiten zu laden.
     *
     * @param bauvorhabenId des Bauvorhabens
     * @return ID und Name der Abfragen, welche das Bauvorhaben referenzieren.
     */
    @Query(
        "SELECT abfrage.id AS id, abfrage.name AS name FROM Abfrage abfrage WHERE abfrage.bauvorhaben.id = :bauvorhabenId"
    )
    List<AbfrageIdAndName> findIdsAndNamesByBauvorhabenId(@Param("bauvorhabenId") final UUID bauvorhabenId);

    /**
     * Projektion auf ID und Name einer Abfrage.
     */
    interface AbfrageIdAndName {
        UUID getId();

        String getName();
    }
}

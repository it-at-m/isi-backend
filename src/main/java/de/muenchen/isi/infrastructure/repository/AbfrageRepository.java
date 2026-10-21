/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbfrageRepository extends JpaRepository<Abfrage, UUID> {
    Stream<Abfrage> findAllByBauvorhabenId(final UUID id);

    Stream<Abfrage> findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(final UUID id);

    /**
     * Ermittelt ausschließlich den Status einer Abfrage, ohne die Abfrage samt Abhängigkeiten zu laden.
     *
     * @param id der Abfrage
     * @return den Status der Abfrage oder ein leeres {@link Optional} falls die Abfrage nicht existiert.
     */
    @Query("SELECT abfrage.statusAbfrage FROM Abfrage abfrage WHERE abfrage.id = :id")
    Optional<StatusAbfrage> findStatusAbfrageById(@Param("id") final UUID id);
}

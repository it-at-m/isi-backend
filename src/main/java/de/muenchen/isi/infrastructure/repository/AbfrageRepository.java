/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Abfrage;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbfrageRepository extends JpaRepository<Abfrage, UUID> {
    Stream<Abfrage> findAllByBauvorhabenId(final UUID id);

    Stream<Abfrage> findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(final UUID id);

    Optional<Abfrage> findByNameIgnoreCase(final String nameAbfrage);
}

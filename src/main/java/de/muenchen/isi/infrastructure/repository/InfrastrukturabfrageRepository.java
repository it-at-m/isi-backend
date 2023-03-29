/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfrastrukturabfrageRepository extends JpaRepository<Infrastrukturabfrage, UUID> {
    Stream<Infrastrukturabfrage> findAllByOrderByAbfrageFristStellungnahmeDesc();

    Stream<Infrastrukturabfrage> findAllByAbfrageBauvorhabenId(final UUID id);

    Optional<Infrastrukturabfrage> findByAbfrage_NameAbfrageIgnoreCase(final String nameAbfrage);
}

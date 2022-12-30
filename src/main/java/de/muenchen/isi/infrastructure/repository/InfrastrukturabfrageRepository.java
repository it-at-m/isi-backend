/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface InfrastrukturabfrageRepository extends PagingAndSortingRepository<Infrastrukturabfrage, UUID> {

    Stream<Infrastrukturabfrage> findAllByOrderByAbfrageFristStellungnahmeDesc();

    Stream<Infrastrukturabfrage> findAllByAbfrageBauvorhabenId(final UUID id);

}

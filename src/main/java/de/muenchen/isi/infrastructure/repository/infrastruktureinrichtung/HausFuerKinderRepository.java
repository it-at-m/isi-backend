/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface HausFuerKinderRepository extends PagingAndSortingRepository<HausFuerKinder, UUID> {

    Stream<HausFuerKinder> findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc();

    Stream<HausFuerKinder> findAllByInfrastruktureinrichtungBauvorhabenId(final UUID id);

}

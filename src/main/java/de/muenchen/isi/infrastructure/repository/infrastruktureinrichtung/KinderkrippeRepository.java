/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface KinderkrippeRepository extends JpaRepository<Kinderkrippe, UUID> {

    Stream<Kinderkrippe> findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc();

    Stream<Kinderkrippe> findAllByInfrastruktureinrichtungBauvorhabenId(final UUID id);

}

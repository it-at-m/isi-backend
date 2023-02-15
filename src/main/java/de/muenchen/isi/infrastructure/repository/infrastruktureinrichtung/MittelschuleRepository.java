/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.stream.Stream;

public interface MittelschuleRepository extends JpaRepository<Mittelschule, UUID> {

    Stream<Mittelschule> findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc();

    Stream<Mittelschule> findAllByInfrastruktureinrichtungBauvorhabenId(final UUID id);

}

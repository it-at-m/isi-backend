/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KindergartenRepository extends JpaRepository<Kindergarten, UUID> {
    Stream<Kindergarten> findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc();

    Stream<Kindergarten> findAllByInfrastruktureinrichtungBauvorhabenId(final UUID id);
}

/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfrastruktureinrichtungRepository extends JpaRepository<Infrastruktureinrichtung, UUID> {
    Stream<Infrastruktureinrichtung> findAllByOrderByNameEinrichtungAsc();

    Stream<Infrastruktureinrichtung> findAllByBauvorhabenId(final UUID id);
}

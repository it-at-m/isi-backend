/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrundschuleRepository extends JpaRepository<Grundschule, UUID> {
    Stream<Grundschule> findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc();

    Stream<Grundschule> findAllByInfrastruktureinrichtungBauvorhabenId(final UUID id);
}

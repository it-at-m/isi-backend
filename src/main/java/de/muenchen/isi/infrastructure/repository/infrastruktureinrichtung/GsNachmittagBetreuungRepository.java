/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GsNachmittagBetreuungRepository extends JpaRepository<GsNachmittagBetreuung, UUID> {
    Stream<GsNachmittagBetreuung> findAllByOrderByInfrastruktureinrichtungNameEinrichtungAsc();

    Stream<GsNachmittagBetreuung> findAllByInfrastruktureinrichtungBauvorhabenId(final UUID id);
}

/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface DokumentDomainMapper {
    DokumentModel entity2Model(final Dokument entity);

    List<DokumentModel> entity2Model(final List<Dokument> entities);

    Dokument model2Entity(final DokumentModel model);
}

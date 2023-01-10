/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(
        config = MapstructConfiguration.class
)
public interface DokumentDomainMapper {

    DokumentModel entity2Model(final Dokument entity);

    Set<DokumentModel> entity2Model(Iterable<Dokument> entities);

    Dokument model2Entity(final DokumentModel model);

}

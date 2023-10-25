/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.dto.filehandling.DokumenteDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.filehandling.DokumentModel;
import de.muenchen.isi.domain.model.filehandling.DokumenteModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface DokumentApiMapper {
    DokumenteDto model2Dto(final DokumenteModel model);

    DokumentDto model2Dto(final DokumentModel model);

    DokumentModel dto2Model(final DokumentDto model);
}

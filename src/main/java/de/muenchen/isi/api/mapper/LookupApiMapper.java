/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.stammdaten.LookupListsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.stammdaten.LookupListsModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface LookupApiMapper {

    LookupListsDto model2Dto(final LookupListsModel model);

}

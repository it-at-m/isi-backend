/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.InfrastrukturabfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateDto;
import de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.list.AbfrageListElementDto;
import de.muenchen.isi.api.dto.list.AbfrageListElementsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.list.AbfrageListElementModel;
import de.muenchen.isi.domain.model.list.AbfrageListElementsModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class, uses = { AbfragevarianteApiMapper.class, DokumentApiMapper.class })
public interface AbfrageApiMapper {
    @Mapping(target = "abfrage.bauvorhaben", ignore = true)
    InfrastrukturabfrageDto model2Dto(final InfrastrukturabfrageModel model);

    @AfterMapping
    default void setBauvorhabenIdOnInfrastrukturabfrageDto(
        final InfrastrukturabfrageModel model,
        @MappingTarget final InfrastrukturabfrageDto dto
    ) {
        final BauvorhabenModel bauvorhabenModel = model.getAbfrage().getBauvorhaben();
        dto.getAbfrage().setBauvorhaben(bauvorhabenModel != null ? bauvorhabenModel.getId() : null);
    }

    @Mapping(target = "bauvorhaben", ignore = true)
    AbfrageAngelegtModel dto2Model(final AbfrageAngelegtDto dto);

    InfrastrukturabfrageAngelegtModel dto2Model(final InfrastrukturabfrageAngelegtDto dto);

    InfrastrukturabfrageInBearbeitungSachbearbeitungModel dto2Model(
        final InfrastrukturabfrageInBearbeitungSachbearbeitungDto dto
    );

    InfrastrukturabfrageInBearbeitungFachreferateModel dto2Model(
        final InfrastrukturabfrageInBearbeitungFachreferateDto dto
    );

    AbfrageListElementsDto model2Dto(final AbfrageListElementsModel model);

    AbfrageListElementDto model2ListElementDto(final AbfrageListElementModel model);
}

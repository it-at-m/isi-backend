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
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class, uses = { AbfragevarianteApiMapper.class, DokumentApiMapper.class })
public interface AbfrageAltApiMapper {
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
}

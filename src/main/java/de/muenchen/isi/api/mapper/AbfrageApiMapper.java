/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.InfrastrukturabfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.list.AbfrageListElementsDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
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

    @AfterMapping
    default void setAbfragevariantenNrDisplayOnAbfragevarianteDto(
        final InfrastrukturabfrageModel model,
        @MappingTarget final InfrastrukturabfrageDto dto
    ) {
        dto
            .getAbfragevarianten()
            .forEach(abfragevarianteDto ->
                abfragevarianteDto.setAbfragevariantenNrDisplay(
                    String.format("1.%s", abfragevarianteDto.getAbfragevariantenNr())
                )
            );
        if (dto.getAbfragevariantenSachbearbeitung() != null) {
            dto
                .getAbfragevariantenSachbearbeitung()
                .forEach(abfragevarianteDto ->
                    abfragevarianteDto.setAbfragevariantenNrDisplay(
                        String.format("2.%s", abfragevarianteDto.getAbfragevariantenNr())
                    )
                );
        }
    }

    @Mapping(target = "bauvorhaben", ignore = true)
    AbfrageAngelegtModel dto2Model(final AbfrageAngelegtDto dto);

    InfrastrukturabfrageAngelegtModel dto2Model(final InfrastrukturabfrageAngelegtDto dto);

    InfrastrukturabfrageInBearbeitungSachbearbeitungModel dto2Model(
        final InfrastrukturabfrageInBearbeitungSachbearbeitungDto dto
    );

    AbfrageListElementsDto model2Dto(final AbfrageListElementsModel model);
}

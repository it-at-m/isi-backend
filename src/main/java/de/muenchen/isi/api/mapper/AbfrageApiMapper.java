package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.api.dto.BauleitplanverfahrenDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapstructConfiguration.class, uses = { AbfragevarianteApiMapper.class, DokumentApiMapper.class })
public interface AbfrageApiMapper {
    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = BauleitplanverfahrenDto.class)
    @Mapping(target = "displayName", ignore = true)
    AbfrageDto model2Dto(final AbfrageModel model);

    @AfterMapping
    default void afterMappingModel2Dto(final AbfrageModel model, @MappingTarget final AbfrageDto dto) {
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(model.getArtAbfrage())) {
            final var bauleitplanverfahren = (BauleitplanverfahrenModel) model;
            dto.setDisplayName(bauleitplanverfahren.getDisplayName());
        }
    }

    @SubclassMapping(source = BauleitplanverfahrenAngelegtDto.class, target = BauleitplanverfahrenAngelegtModel.class)
    AbfrageAngelegtModel dto2Model(final AbfrageAngelegtDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenInBearbeitungSachbearbeitungDto.class,
        target = BauleitplanverfahrenInBearbeitungSachbearbeitungModel.class
    )
    AbfrageInBearbeitungSachbearbeitungModel dto2Model(final AbfrageInBearbeitungSachbearbeitungDto dto);

    @SubclassMapping(
        source = AbfrageInBearbeitungFachreferatDto.class,
        target = AbfrageInBearbeitungFachreferatModel.class
    )
    AbfrageInBearbeitungFachreferatModel dto2Model(final AbfrageInBearbeitungFachreferatDto dto);
}

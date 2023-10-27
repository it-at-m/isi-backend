package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.api.dto.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.api.dto.BauleitplanverfahrenDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapstructConfiguration.class, uses = { DokumentApiMapper.class })
public interface AbfrageApiMapper {
    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = BauleitplanverfahrenDto.class)
    @SubclassMapping(source = BaugenehmigungsverfahrenModel.class, target = BaugenehmigungsverfahrenDto.class)
    @Mapping(target = "displayName", ignore = true)
    AbfrageDto model2Dto(final AbfrageModel model);

    @AfterMapping
    default void afterMappingModel2Dto(final AbfrageModel model, @MappingTarget final AbfrageDto dto) {
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(model.getArtAbfrage())) {
            final var bauleitplanverfahren = (BauleitplanverfahrenModel) model;
            dto.setDisplayName(bauleitplanverfahren.getDisplayName());
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(model.getArtAbfrage())) {
            final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenModel) model;
            dto.setDisplayName(baugenehmigungsverfahren.getDisplayName());
        }
    }

    @SubclassMapping(source = BauleitplanverfahrenAngelegtDto.class, target = BauleitplanverfahrenAngelegtModel.class)
    @SubclassMapping(
        source = BaugenehmigungsverfahrenAngelegtDto.class,
        target = BaugenehmigungsverfahrenAngelegtModel.class
    )
    AbfrageAngelegtModel dto2Model(final AbfrageAngelegtDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenInBearbeitungSachbearbeitungDto.class,
        target = BauleitplanverfahrenInBearbeitungSachbearbeitungModel.class
    )
    AbfrageInBearbeitungSachbearbeitungModel dto2Model(final AbfrageInBearbeitungSachbearbeitungDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenInBearbeitungFachreferatDto.class,
        target = BauleitplanverfahrenInBearbeitungFachreferatModel.class
    )
    AbfrageInBearbeitungFachreferatModel dto2Model(final AbfrageInBearbeitungFachreferatDto dto);
}

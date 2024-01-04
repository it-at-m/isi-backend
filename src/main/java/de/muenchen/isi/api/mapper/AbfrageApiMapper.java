package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.api.dto.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.api.dto.BauleitplanverfahrenDto;
import de.muenchen.isi.api.dto.WeiteresVerfahrenDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.WeiteresVerfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt.AbfrageBedarfsmeldungErfolgtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt.BaugenehmigungsverfahrenBedarfsmeldungErfolgtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt.BauleitplanverfahrenBedarfsmeldungErfolgtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt.WeiteresVerfahrenBedarfsmeldungErfolgtDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.BaugenehmigungsverfahrenInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.WeiteresVerfahrenInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.AbfrageBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.BaugenehmigungsverfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.BauleitplanverfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungErfolgt.WeiteresVerfahrenBedarfsmeldungErfolgtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.WeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungModel;
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
    @SubclassMapping(source = WeiteresVerfahrenModel.class, target = WeiteresVerfahrenDto.class)
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
        } else if (ArtAbfrage.WEITERES_VERFAHREN.equals(model.getArtAbfrage())) {
            final var weiteresVerfahren = (WeiteresVerfahrenModel) model;
            dto.setDisplayName(weiteresVerfahren.getDisplayName());
        }
    }

    @SubclassMapping(source = BauleitplanverfahrenAngelegtDto.class, target = BauleitplanverfahrenAngelegtModel.class)
    @SubclassMapping(
        source = BaugenehmigungsverfahrenAngelegtDto.class,
        target = BaugenehmigungsverfahrenAngelegtModel.class
    )
    @SubclassMapping(source = WeiteresVerfahrenAngelegtDto.class, target = WeiteresVerfahrenAngelegtModel.class)
    AbfrageAngelegtModel dto2Model(final AbfrageAngelegtDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenInBearbeitungSachbearbeitungDto.class,
        target = BauleitplanverfahrenInBearbeitungSachbearbeitungModel.class
    )
    @SubclassMapping(
        source = BaugenehmigungsverfahrenInBearbeitungSachbearbeitungDto.class,
        target = BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel.class
    )
    @SubclassMapping(
        source = WeiteresVerfahrenInBearbeitungSachbearbeitungDto.class,
        target = WeiteresVerfahrenInBearbeitungSachbearbeitungModel.class
    )
    AbfrageInBearbeitungSachbearbeitungModel dto2Model(final AbfrageInBearbeitungSachbearbeitungDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenInBearbeitungFachreferatDto.class,
        target = BauleitplanverfahrenInBearbeitungFachreferatModel.class
    )
    @SubclassMapping(
        source = BaugenehmigungsverfahrenInBearbeitungFachreferatDto.class,
        target = BaugenehmigungsverfahrenInBearbeitungFachreferatModel.class
    )
    @SubclassMapping(
        source = WeiteresVerfahrenInBearbeitungFachreferatDto.class,
        target = WeiteresVerfahrenInBearbeitungFachreferatModel.class
    )
    AbfrageInBearbeitungFachreferatModel dto2Model(final AbfrageInBearbeitungFachreferatDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenBedarfsmeldungErfolgtDto.class,
        target = BauleitplanverfahrenBedarfsmeldungErfolgtModel.class
    )
    @SubclassMapping(
        source = BaugenehmigungsverfahrenBedarfsmeldungErfolgtDto.class,
        target = BaugenehmigungsverfahrenBedarfsmeldungErfolgtModel.class
    )
    @SubclassMapping(
        source = WeiteresVerfahrenBedarfsmeldungErfolgtDto.class,
        target = WeiteresVerfahrenBedarfsmeldungErfolgtModel.class
    )
    AbfrageBedarfsmeldungErfolgtModel dto2Model(final AbfrageBedarfsmeldungErfolgtDto dto);
}

package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.api.dto.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.api.dto.BauleitplanverfahrenDto;
import de.muenchen.isi.api.dto.WeiteresVerfahrenDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.WeiteresVerfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageEinpflegenBedarfsmeldung.AbfrageEinpflegenBedarfsmeldungDto;
import de.muenchen.isi.api.dto.abfrageEinpflegenBedarfsmeldung.BauleitplanverfahrenEinpflegenBedarfsmeldungDto;
import de.muenchen.isi.api.dto.abfrageEinpflegenBedarfsmeldung.WeiteresVerfahrenEinpflegenBedarfsmeldungDto;
import de.muenchen.isi.api.dto.abfrageEinplanungBedarfe.AbfrageEinplanungBedarfeDto;
import de.muenchen.isi.api.dto.abfrageEinplanungBedarfe.BaugenehmigungsverfahrenEinplanungBedarfeDto;
import de.muenchen.isi.api.dto.abfrageEinplanungBedarfe.BauleitplanverfahrenEinplanungBedarfeDto;
import de.muenchen.isi.api.dto.abfrageEinplanungBedarfe.WeiteresVerfahrenEinplanungBedarfeDto;
import de.muenchen.isi.api.dto.abfrageEinpflegenBedarfsmeldung.BaugenehmigungsverfahrenEinpflegenBedarfsmeldungDto;
import de.muenchen.isi.api.dto.abfrageStartBearbeitung.AbfrageStartBearbeitungDto;
import de.muenchen.isi.api.dto.abfrageStartBearbeitung.BaugenehmigungsverfahrenStartBearbeitungDto;
import de.muenchen.isi.api.dto.abfrageStartBearbeitung.BauleitplanverfahrenStartBearbeitungDto;
import de.muenchen.isi.api.dto.abfrageStartBearbeitung.WeiteresVerfahrenStartBearbeitungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.AbfrageEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.BauleitplanverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.AbfrageEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.BaugenehmigungsverfahrenEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.BauleitplanverfahrenEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageEinplanungBedarfe.WeiteresVerfahrenEinplanungBedarfeModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.BaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageEinpflegenBedarfsmeldung.WeiteresVerfahrenEinpflegenBedarfsmeldungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.AbfrageStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.BaugenehmigungsverfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.BauleitplanverfahrenStartBearbeitungModel;
import de.muenchen.isi.domain.model.abfrageStartBearbeitung.WeiteresVerfahrenStartBearbeitungModel;
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
        source = BauleitplanverfahrenStartBearbeitungDto.class,
        target = BauleitplanverfahrenStartBearbeitungModel.class
    )
    @SubclassMapping(
        source = BaugenehmigungsverfahrenStartBearbeitungDto.class,
        target = BaugenehmigungsverfahrenStartBearbeitungModel.class
    )
    @SubclassMapping(
        source = WeiteresVerfahrenStartBearbeitungDto.class,
        target = WeiteresVerfahrenStartBearbeitungModel.class
    )
    AbfrageStartBearbeitungModel dto2Model(final AbfrageStartBearbeitungDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenEinpflegenBedarfsmeldungDto.class,
        target = BauleitplanverfahrenEinpflegenBedarfsmeldungModel.class
    )
    @SubclassMapping(
        source = BaugenehmigungsverfahrenEinpflegenBedarfsmeldungDto.class,
        target = BaugenehmigungsverfahrenEinpflegenBedarfsmeldungModel.class
    )
    @SubclassMapping(
        source = WeiteresVerfahrenEinpflegenBedarfsmeldungDto.class,
        target = WeiteresVerfahrenEinpflegenBedarfsmeldungModel.class
    )
    AbfrageEinpflegenBedarfsmeldungModel dto2Model(final AbfrageEinpflegenBedarfsmeldungDto dto);

    @SubclassMapping(
        source = BauleitplanverfahrenEinplanungBedarfeDto.class,
        target = BauleitplanverfahrenEinplanungBedarfeModel.class
    )
    @SubclassMapping(
        source = BaugenehmigungsverfahrenEinplanungBedarfeDto.class,
        target = BaugenehmigungsverfahrenEinplanungBedarfeModel.class
    )
    @SubclassMapping(
        source = WeiteresVerfahrenEinplanungBedarfeDto.class,
        target = WeiteresVerfahrenEinplanungBedarfeModel.class
    )
    AbfrageEinplanungBedarfeModel dto2Model(final AbfrageEinplanungBedarfeDto dto);
}

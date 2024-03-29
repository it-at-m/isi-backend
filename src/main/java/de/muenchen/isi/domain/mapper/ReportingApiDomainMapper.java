package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteBaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteBauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteWeiteresVerfahrenDto;
import de.muenchen.isi.reporting.client.model.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.BauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.LangfristigerBedarfDto;
import de.muenchen.isi.reporting.client.model.WeiteresVerfahrenDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
    config = MapstructConfiguration.class,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface ReportingApiDomainMapper {
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = BauleitplanverfahrenDto.class)
    @SubclassMapping(source = BaugenehmigungsverfahrenModel.class, target = BaugenehmigungsverfahrenDto.class)
    @SubclassMapping(source = WeiteresVerfahrenModel.class, target = WeiteresVerfahrenDto.class)
    AbfrageDto model2ReportingDto(final AbfrageModel model);

    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = true)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = true)
    AbfragevarianteBauleitplanverfahrenDto model2ReportingDto(final AbfragevarianteBauleitplanverfahrenModel model);

    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = true)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = true)
    AbfragevarianteBaugenehmigungsverfahrenDto model2ReportingDto(
        final AbfragevarianteBaugenehmigungsverfahrenModel model
    );

    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = true)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = true)
    AbfragevarianteWeiteresVerfahrenDto model2ReportingDto(final AbfragevarianteWeiteresVerfahrenModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "artAbfragevariante", ignore = true)
    @Mapping(target = "abfragevariantenNr", ignore = true)
    @Mapping(target = "name", ignore = true)
    AbfragevarianteBauleitplanverfahrenDto reportingDtoAndBedarfe2ReportingDto(
        @MappingTarget final AbfragevarianteBauleitplanverfahrenDto dto,
        final BedarfeForAbfragevarianteModel bedarfeForAbfragevariante
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "artAbfragevariante", ignore = true)
    @Mapping(target = "abfragevariantenNr", ignore = true)
    @Mapping(target = "name", ignore = true)
    AbfragevarianteBaugenehmigungsverfahrenDto reportingDtoAndBedarfe2ReportingDto(
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenDto dto,
        final BedarfeForAbfragevarianteModel bedarfeForAbfragevariante
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "artAbfragevariante", ignore = true)
    @Mapping(target = "abfragevariantenNr", ignore = true)
    @Mapping(target = "name", ignore = true)
    AbfragevarianteWeiteresVerfahrenDto reportingDtoAndBedarfe2ReportingDto(
        @MappingTarget final AbfragevarianteWeiteresVerfahrenDto dto,
        final BedarfeForAbfragevarianteModel bedarfeForAbfragevariante
    );

    LangfristigerBedarfDto model2ReportingDto(final LangfristigerBedarfModel model);

    AbfrageDto.ArtAbfrageEnum artAbfrage2ArtAbfrageReporting(final ArtAbfrage artAbfrage);
}

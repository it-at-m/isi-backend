package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteBaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteBauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.AbfragevarianteWeiteresVerfahrenDto;
import de.muenchen.isi.reporting.client.model.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.BauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.LangfristigerBedarfDto;
import de.muenchen.isi.reporting.client.model.WeiteresVerfahrenDto;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
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
    @Mapping(source = "bauvorhaben", target = "bauvorhabenId")
    @SubclassMapping(source = BauleitplanverfahrenModel.class, target = BauleitplanverfahrenDto.class)
    @SubclassMapping(source = BaugenehmigungsverfahrenModel.class, target = BaugenehmigungsverfahrenDto.class)
    @SubclassMapping(source = WeiteresVerfahrenModel.class, target = WeiteresVerfahrenDto.class)
    AbfrageDto model2ReportingDto(final AbfrageModel model);

    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = true)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = true)
    @Mapping(target = "wesentlicheRechtsgrundlage", ignore = true)
    @Mapping(target = "wesentlicheRechtsgrundlageFreieEingabe", ignore = true)
    AbfragevarianteBauleitplanverfahrenDto model2ReportingDto(final AbfragevarianteBauleitplanverfahrenModel model);

    @AfterMapping
    default void model2ReportingDto(
        final AbfragevarianteBauleitplanverfahrenModel model,
        @MappingTarget final AbfragevarianteBauleitplanverfahrenDto dto
    ) {
        dto.setWesentlicheRechtsgrundlage(
            model.getPlanart().stream().map(WesentlicheRechtsgrundlage::getBezeichnung).collect(Collectors.toList())
        );
        dto.setWesentlicheRechtsgrundlageFreieEingabe((model.getPlanartFreieEingabe()));
    }

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

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = false)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = false)
    AbfragevarianteBauleitplanverfahrenDto reportingDtoAndBedarfe2ReportingDto(
        @MappingTarget final AbfragevarianteBauleitplanverfahrenDto dto,
        final BedarfeForAbfragevarianteModel bedarfeForAbfragevariante
    );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = false)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = false)
    AbfragevarianteBaugenehmigungsverfahrenDto reportingDtoAndBedarfe2ReportingDto(
        @MappingTarget final AbfragevarianteBaugenehmigungsverfahrenDto dto,
        final BedarfeForAbfragevarianteModel bedarfeForAbfragevariante
    );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "langfristigerPlanungsursaechlicherBedarf", ignore = false)
    @Mapping(target = "langfristigerSobonursaechlicherBedarf", ignore = false)
    AbfragevarianteWeiteresVerfahrenDto reportingDtoAndBedarfe2ReportingDto(
        @MappingTarget final AbfragevarianteWeiteresVerfahrenDto dto,
        final BedarfeForAbfragevarianteModel bedarfeForAbfragevariante
    );

    LangfristigerBedarfDto model2ReportingDto(final LangfristigerBedarfModel model);

    AbfrageDto.ArtAbfrageEnum artAbfrage2ArtAbfrageReporting(final ArtAbfrage artAbfrage);
}

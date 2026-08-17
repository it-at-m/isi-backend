package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AbfrageConverterMapperConfig {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "sub", ignore = true)
    @Mapping(target = "statusAbfrage", ignore = true)
    @Mapping(target = "artAbfrage", ignore = true)
    @Mapping(target = "bearbeitungshistorie", ignore = true)
    void ignoreCommonFields(AbfrageModel source, @MappingTarget AbfrageModel target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "artAbfrage", ignore = true)
    void ignoreCommonFields(AbfragevarianteModel source, @MappingTarget AbfragevarianteModel target);
}

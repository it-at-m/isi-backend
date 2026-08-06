package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.domain.model.AbfrageModel;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@MapperConfig
public interface AbfrageMapperConfig {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "sub", ignore = true)
    @Mapping(target = "statusAbfrage", ignore = true)
    @Mapping(target = "artAbfrage", ignore = true)
    @Mapping(target = "bearbeitungshistorie", ignore = true)
    void ignoreCommonFields(AbfrageModel source, @MappingTarget AbfrageModel target);
}

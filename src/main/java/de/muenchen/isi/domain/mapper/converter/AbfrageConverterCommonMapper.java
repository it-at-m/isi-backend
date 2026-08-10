package de.muenchen.isi.domain.mapper.converter;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = AbfrageConverterMapperConfig.class)
public abstract class AbfrageConverterCommonMapper {

    public abstract void mapCommon(AbfrageModel source, @MappingTarget AbfrageModel target);

    @AfterMapping
    protected void applyDefaults(AbfrageModel source, @MappingTarget AbfrageModel target) {
        target.setStatusAbfrage(StatusAbfrage.ANGELEGT);
    }
}

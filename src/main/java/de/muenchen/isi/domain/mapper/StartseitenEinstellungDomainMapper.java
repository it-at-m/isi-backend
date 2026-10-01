package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.infrastructure.entity.startseite.StartseitenEinstellung;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface StartseitenEinstellungDomainMapper {
    StartseitenEinstellungModel entity2Model(final StartseitenEinstellung startseitenEinstellung);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "lastModifiedDateTime", ignore = true)
    @Mapping(target = "personalID", ignore = true)
    void updateEntityFromModel(
        final StartseitenEinstellungModel model,
        @MappingTarget final StartseitenEinstellung entity
    );
}

package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import de.muenchen.isi.infrastructure.entity.benutzer.StartseitenEinstellung;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface BenutzerDomainMapper {
    StartseitenEinstellungModel entity2Model(final StartseitenEinstellung startseitenEinstellung);

    StartseitenEinstellung model2Entity(final StartseitenEinstellungModel startseitenEinstellungModel);
}

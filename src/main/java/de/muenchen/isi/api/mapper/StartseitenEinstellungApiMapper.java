package de.muenchen.isi.api.mapper;

import de.muenchen.isi.api.dto.startseite.StartseitenEinstellungDto;
import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.startseite.StartseitenEinstellungModel;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface StartseitenEinstellungApiMapper {
    StartseitenEinstellungDto model2Dto(final StartseitenEinstellungModel startseitenEinstellungModel);

    StartseitenEinstellungModel dto2Model(final StartseitenEinstellungDto startseitenEinstellungDto);
}

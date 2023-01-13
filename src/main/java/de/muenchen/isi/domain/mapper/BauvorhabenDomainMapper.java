package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import org.mapstruct.Mapper;

@Mapper(
        config = MapstructConfiguration.class,
        uses = {DokumentDomainMapper.class}
)
public interface BauvorhabenDomainMapper {

    BauvorhabenModel entity2Model(final Bauvorhaben bauvorhaben);

    Bauvorhaben model2Entity(final BauvorhabenModel bauvorhabenModel);
}

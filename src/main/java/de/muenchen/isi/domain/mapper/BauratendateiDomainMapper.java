package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;

@Mapper(config = MapstructConfiguration.class)
public interface BauratendateiDomainMapper {
    @DeepClone
    BauratendateiInputModel cloneDeep(final BauratendateiInputModel model);
}

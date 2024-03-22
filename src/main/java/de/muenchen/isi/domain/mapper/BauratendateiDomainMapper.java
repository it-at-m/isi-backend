package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiWohneinheitenModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.control.DeepClone;

@Mapper(config = MapstructConfiguration.class)
public interface BauratendateiDomainMapper {
    @DeepClone
    BauratendateiInputModel cloneDeep(final BauratendateiInputModel model);

    @Mappings(
        {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "createdDateTime", ignore = true),
            @Mapping(target = "lastModifiedDateTime", ignore = true),
        }
    )
    BauratendateiWohneinheitenModel map(final WohneinheitenProFoerderartProJahrModel wohneinheitenProFoerderartProJahr);
}

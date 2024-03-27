package de.muenchen.isi.domain.mapper;

import de.muenchen.isi.configuration.MapstructConfiguration;
import de.muenchen.isi.domain.model.bauratendatei.BauratendateiInputModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.bauratendatei.BauratendateiInput;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;

@Mapper(config = MapstructConfiguration.class)
public interface BauratendateiDomainMapper {
    @DeepClone
    BauratendateiInputModel cloneDeep(final BauratendateiInputModel model);

    @DeepClone
    List<WohneinheitenProFoerderartProJahrModel> cloneDeep(final List<WohneinheitenProFoerderartProJahrModel> model);

    @DeepClone
    WohneinheitenProFoerderartProJahrModel cloneDeep(final WohneinheitenProFoerderartProJahrModel model);

    BauratendateiInputModel entity2Model(final BauratendateiInput entity);
}
